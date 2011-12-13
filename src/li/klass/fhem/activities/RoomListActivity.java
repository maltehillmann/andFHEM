package li.klass.fhem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import li.klass.fhem.R;
import li.klass.fhem.adapter.RoomListAdapter;
import li.klass.fhem.data.FHEMService;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends UpdateableActivity<List<String>> {

    private RoomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.room_list);

        ListView roomList = (ListView) findViewById(R.id.roomList);

        adapter = new RoomListAdapter(this, R.layout.room, new ArrayList<String>());
        roomList.setAdapter(adapter);

        update(false);

        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String roomName = String.valueOf(view.getTag());
                Intent intent = new Intent();
                intent.setClass(RoomListActivity.this, RoomDetailActivity.class);
                intent.putExtras(new Bundle());
                intent.putExtra("roomName", roomName);

                startActivity(intent);
            }
        });
    }

    @Override
    protected List<String> getCurrentData(boolean refresh) {
        return FHEMService.INSTANCE.getRoomList(refresh);
    }

    @Override
    protected void updateData(List<String> data) {
        adapter.updateData(data);
    }
}
