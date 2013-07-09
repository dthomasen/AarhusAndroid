package dk.dthomasen.slidingmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import dk.dthomasen.aarhus.activity.FitnessIDetFri;
import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.activity.Hundeskove;
import dk.dthomasen.aarhus.activity.Kiosker;
import dk.dthomasen.aarhus.activity.Legepladser;
import dk.dthomasen.aarhus.activity.MainActivity;
import dk.dthomasen.aarhus.activity.Settings;
import dk.dthomasen.aarhus.activity.Shelters;

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

        Section programSection = new Section("Diverse");
        programSection.addSectionItem(001, "Hvad sker i Aarhus", Integer.toString(R.drawable.news));
        programSection.addSectionItem(002, "Indstillinger", Integer.toString(R.drawable.settings));

        Section aktiviteterSection = new Section("Aktiviteter");
        aktiviteterSection.addSectionItem(101, "Fitness i det fri", Integer.toString(R.drawable.fitness));
        aktiviteterSection.addSectionItem(102, "Legepladser", Integer.toString(R.drawable.playground));
        aktiviteterSection.addSectionItem(103, "Kiosker", Integer.toString(R.drawable.kiosk));
        Section skoveSection = new Section("Skove & Parker");
        skoveSection.addSectionItem(201, "Skove", Integer.toString(R.drawable.forrest));
        skoveSection.addSectionItem(202, "Parker", Integer.toString(R.drawable.sun));
        skoveSection.addSectionItem(203, "Bålpladser", Integer.toString(R.drawable.fire));
        skoveSection.addSectionItem(204, "Shelters/Madpakkehuse", Integer.toString(R.drawable.shelter));
        skoveSection.addSectionItem(205, "Hundeskove", Integer.toString(R.drawable.dog));

        sectionList.add(programSection);
        sectionList.add(aktiviteterSection);
        sectionList.add(skoveSection);
        return sectionList;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        Intent activityIntent = new Intent();
        switch ((int)id) {
            case 001:
                activityIntent = new Intent(this.getActivity(), MainActivity.class);
                break;
            case 002:
                activityIntent = new Intent(this.getActivity(), Settings.class);
                break;
            case 101:
                activityIntent = new Intent(this.getActivity(), FitnessIDetFri.class);
                break;
            case 102:
                activityIntent = new Intent(this.getActivity(), Legepladser.class);
                break;
            case 103:
                activityIntent = new Intent(this.getActivity(), Kiosker.class);
                break;
            case 201:
                //Skove
                break;
            case 202:
                //Parker
                break;
            case 203:
                //Bålpladser
                break;
            case 204:
                activityIntent = new Intent(this.getActivity(), Shelters.class);
                break;
            case 205:
                activityIntent = new Intent(this.getActivity(), Hundeskove.class);
                break;
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(activityIntent);
        return false;
    }
}