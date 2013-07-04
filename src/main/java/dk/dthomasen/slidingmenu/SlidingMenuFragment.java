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
import dk.dthomasen.aarhus.activity.MainActivity;

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

        Section findSection = new Section("Find");
        findSection.addSectionItem(101, "Fitness i det fri", Integer.toString(R.drawable.fitness));
        findSection.addSectionItem(102, "Hundeskove", Integer.toString(R.drawable.dog));
        findSection.addSectionItem(103, "Legepladser", Integer.toString(R.drawable.playground));
        findSection.addSectionItem(104, "Shelters/Madpakkehuse", Integer.toString(R.drawable.shelter));
        findSection.addSectionItem(105, "Skove/Parker", Integer.toString(R.drawable.forrest));
        findSection.addSectionItem(106, "Bålpladser", Integer.toString(R.drawable.fire));

        sectionList.add(programSection);
        sectionList.add(findSection);
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
                break;
            case 101:
                activityIntent = new Intent(this.getActivity(), FitnessIDetFri.class);
                break;
            case 102:
                activityIntent = new Intent(this.getActivity(), Hundeskove.class);
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
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(activityIntent);
        return false;
    }
}