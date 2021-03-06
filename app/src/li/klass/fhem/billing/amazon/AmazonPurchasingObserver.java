/*
 * AndFHEM - Open Source Android application to control a FHEM home automation
 * server.
 *
 * Copyright (c) 2012, Matthias Klass or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLICLICENSE, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU GENERAL PUBLIC LICENSE
 * for more details.
 *
 * You should have received a copy of the GNU GENERAL PUBLIC LICENSE
 * along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 */

package li.klass.fhem.billing.amazon;

import android.content.Intent;
import android.util.Log;
import com.amazon.inapp.purchasing.*;
import li.klass.fhem.AndFHEMApplication;
import li.klass.fhem.billing.BillingConstants;
import li.klass.fhem.billing.BillingService;
import li.klass.fhem.constants.Actions;

import java.util.Set;

public class AmazonPurchasingObserver extends BasePurchasingObserver {
    public static final AmazonPurchasingObserver INSTANCE = new AmazonPurchasingObserver();
    public static final String TAG = AmazonPurchasingObserver.class.getName();

    private AmazonPurchasingObserver() {
        super(AndFHEMApplication.getContext());
        PurchasingManager.registerObserver(this);
    }

    @Override
    public void onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse) {
        super.onPurchaseUpdatesResponse(purchaseUpdatesResponse);
        if (purchaseUpdatesResponse.getPurchaseUpdatesRequestStatus()
                != PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus.SUCCESSFUL) return;

        for (String revokedSku : purchaseUpdatesResponse.getRevokedSkus()) {
            BillingService.INSTANCE.markProductAsPurchased(revokedSku, revokedSku, BillingConstants.PurchaseState.REFUNDED,
                    System.currentTimeMillis(), null);
        }

        for (Receipt receipt : purchaseUpdatesResponse.getReceipts()) {
            processSuccessfulReceipt(receipt);
        }

        BillingService.INSTANCE.setBillingDatabaseInitialised(true);

        doUpdate();
    }

    @Override
    public void onPurchaseResponse(PurchaseResponse purchaseResponse) {
        super.onPurchaseResponse(purchaseResponse);

        Receipt receipt = purchaseResponse.getReceipt();

        String sku = receipt != null ? receipt.getSku() : "invalid";

        switch (purchaseResponse.getPurchaseRequestStatus()) {
            case ALREADY_ENTITLED:
                Log.e(TAG, "already entitled for product " + sku);
                processSuccessfulReceipt(receipt);
                break;
            case FAILED:
                Log.e(TAG, "purchase failed, product: " + sku);
                break;
            case INVALID_SKU:
                Log.e(TAG, "invalid product SKU: " + sku);
                break;
            case SUCCESSFUL:
                Log.e(TAG, "purchase successful, product: " + sku);
                processSuccessfulReceipt(receipt);
                break;
        }

        String requestId = purchaseResponse.getRequestId();
        AmazonBillingProvider.INSTANCE.removePurchaseRequest(requestId);

        BillingService.INSTANCE.setBillingDatabaseInitialised(true);

        doUpdate();
    }

    private void processSuccessfulReceipt(Receipt receipt) {
        String sku = receipt.getSku();

        BillingService billingService = BillingService.INSTANCE;

        Set<String> ownedItems = billingService.getOwnedItems();
        if (ownedItems.contains(sku)) return;

        billingService.markProductAsPurchased(sku, sku, BillingConstants.PurchaseState.PURCHASED,
                System.currentTimeMillis(), null);
    }

    private void doUpdate() {
        Intent intent = new Intent(Actions.DO_UPDATE);
        AndFHEMApplication.getContext().sendBroadcast(intent);
    }
}
