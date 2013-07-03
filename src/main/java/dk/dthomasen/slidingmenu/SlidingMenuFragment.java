package dk.dthomasen.slidingmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import dk.dthomasen.aarhus.FitnessIDetFri;
import dk.dthomasen.aarhus.R;

import java.util.ArrayList;
import java.util.List;

public class SlidingMenuFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private ExpandableListView sectionListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<Section> sectionList = createMenu();

        View view = inflater.inflate(R.layout.slidingmenu_fragment, container, false);
        this.sectionListView = (ExpandableListView) view.findViewById(R.id.slidingmenu_view);
        this.sectionListView.setGroupIndicator(null);

        SectionListAdapter sectionListAdapter = new SectionListAdapter(this.getActivity(), sectionList);
        this.sectionListView.setAdapter(sectionListAdapter);

        this.sectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        this.sectionListView.setOnChildClickListener(this);

        int count = sectionListAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            this.sectionListView.expandGroup(position);
        }

        return view;
    }

    private List<Section> createMenu() {
        List<Section> sectionList = new ArrayList<Section>();

        Section fritidSection = new Section("Fritid");
        fritidSection.addSectionItem(101,"Fitness i det fri",Integer.toString(R.drawable.ic_launcher));
        fritidSection.addSectionItem(102, "Hundeskove", Integer.toString(R.drawable.ic_launcher));
        fritidSection.addSectionItem(103, "Legepladser", Integer.toString(R.drawable.ic_launcher));
        fritidSection.addSectionItem(104, "Shelters/Madpakkehuse", Integer.toString(R.drawable.ic_launcher));
        fritidSection.addSectionItem(105, "Skove/Parker", Integer.toString(R.drawable.ic_launcher));
        fritidSection.addSectionItem(106, "Bålpladser", Integer.toString(R.drawable.ic_launcher));

        Section transportSection = new Section("Parkering");
        transportSection.addSectionItem(201, "Invalide parkering", Integer.toString(R.drawable.ic_launcher));
        transportSection.addSectionItem(202, "Lastbil parkering", Integer.toString(R.drawable.ic_launcher));
        transportSection.addSectionItem(203, "MC parkering", Integer.toString(R.drawable.ic_launcher));
        transportSection.addSectionItem(204, "Turistbus parkering", Integer.toString(R.drawable.ic_launcher));

        sectionList.add(fritidSection);
        sectionList.add(transportSection);
        return sectionList;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {

        switch ((int)id) {
            case 101:
                Intent myIntent = new Intent(this.getActivity(), FitnessIDetFri.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(myIntent);
                break;
            case 102:
                //TODO
                break;
            case 103:
                //TODO
                break;
            case 104:
                //TODO
                break;
            case 105:
                //TODO
                break;
            case 106:
                //TODO
                break;
            case 201:
                //TODO
                break;
            case 202:
                //TODO
                break;
            case 203:
                //TODO
                break;
            case 204:
                //TODO
                break;
        }

        return false;
    }
}