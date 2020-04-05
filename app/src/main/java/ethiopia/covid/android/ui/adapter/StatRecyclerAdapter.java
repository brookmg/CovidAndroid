package ethiopia.covid.android.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.CovidStatItem;
import ethiopia.covid.android.data.PatientItem;
import ethiopia.covid.android.data.StatRecyclerItem;
import ethiopia.covid.android.ui.widget.Table;
import ethiopia.covid.android.util.Constant;

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
public class StatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEADER = 0;
    private final int STAT_TABLE = 1;
    private final int PIE_CHART = 2;
    private final int STATUS_CARD = 3;
    private final int PATIENT_TABLE = 4;

    private List<StatRecyclerItem> statRecyclerItemList;

    public StatRecyclerAdapter(List<StatRecyclerItem> statRecyclerItemList) {
        this.statRecyclerItemList = statRecyclerItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_header, parent, false));
            case STAT_TABLE:
                return new CovidStatisticTableViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_table_recycler_element, parent, false)
                );
            case STATUS_CARD:
                return new StatusCardViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.status_card_recycler_element, parent, false)
                );
        }

        return new CovidStatisticTableViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_table_recycler_element, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == STAT_TABLE) {
            position -= 1;
            ((CovidStatisticTableViewHolder) holder).mainCardView.setContentPadding(0,0,0,0);

            List<List<String>> rowItems = new ArrayList<>();
            for (CovidStatItem item : statRecyclerItemList.get(position).getTableItems()) {
                rowItems.add(Arrays.asList(
                        String.valueOf(item.getIdentifier()),
                        String.valueOf(item.getInfected()),
                        String.valueOf(item.getActive()),
                        String.valueOf(item.getDeath()),
                        String.valueOf(item.getRecovered()),
                        String.valueOf(item.getCritical()),
                        String.valueOf(item.getCasePMillion()),
                        String.valueOf(item.getDeathsPMillion())
                ));
            }

            ((CovidStatisticTableViewHolder) holder).mainTable.populateTable(
                    statRecyclerItemList.get(position).getHeaders(),
                    rowItems,
                    statRecyclerItemList.get(position).getFixedHeaderCount() > 0,
                    statRecyclerItemList.get(position).getFixedHeaderCount(),
                    8
            );

        } else if (getItemViewType(position) == STATUS_CARD) {
            position -= 1;
            ((StatusCardViewHolder) holder).country.setText(statRecyclerItemList.get(position).getCountry());

            ((StatusCardViewHolder) holder).infected.setText(String.format(Locale.US, "%d",
                    statRecyclerItemList.get(position).getTotalInfected()));

            ((StatusCardViewHolder) holder).recovered.setText(String.format(Locale.US, "%d",
                    statRecyclerItemList.get(position).getTotalRecovered()));

            ((StatusCardViewHolder) holder).death.setText(String.format(Locale.US, "%d",
                    statRecyclerItemList.get(position).getTotalDeath()));

        } else if (getItemViewType(position) == PATIENT_TABLE) {
            position -= 1;
            ((CovidStatisticTableViewHolder) holder).mainCardView.setContentPadding(0,0,0,0);

            List<List<String>> rowItems = new ArrayList<>();
            for (PatientItem item : statRecyclerItemList.get(position).getPatientItems()) {
                rowItems.add(Arrays.asList(
                        String.valueOf(item.getId()),
                        String.valueOf(item.getName()),
                        String.valueOf(item.getLocation()),
                        String.valueOf(item.getAge()),
                        String.valueOf(item.getGender()),
                        String.valueOf(item.getPatient_nationality()),
                        String.valueOf(item.getRecent_travel_to()),
                        Constant.statusIdentifierMap.get(item.getStatus())
                ));
            }

            ((CovidStatisticTableViewHolder) holder).mainTable.populateTable(
                    statRecyclerItemList.get(position).getHeaders(),
                    rowItems,
                    statRecyclerItemList.get(position).getFixedHeaderCount() > 0,
                    statRecyclerItemList.get(position).getFixedHeaderCount(), 8
            );

        } else {
//            ViewGroup.LayoutParams params = ((HeaderViewHolder) holder).blankView.getLayoutParams();
//            params.height = dpToPx(holder.itemView.getContext(), 116);
//            ((HeaderViewHolder) holder).blankView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return HEADER;
        else {
            switch (statRecyclerItemList.get(position -1).getType()) {
                case 0: return STAT_TABLE;
                case 1: return PIE_CHART;
                case 2: return STATUS_CARD;
                case 3: return PATIENT_TABLE;
                default: return HEADER;
            }
        }
    }

    @Override
    public int getItemCount() {
        return statRecyclerItemList.size()+1;
    }

    private static class CovidStatisticTableViewHolder extends RecyclerView.ViewHolder {
        Table mainTable;
        MaterialCardView mainCardView;

        CovidStatisticTableViewHolder(@NonNull View itemView) {
            super(itemView);
            mainCardView = itemView.findViewById(R.id.main_card_view);
            mainTable = itemView.findViewById(R.id.stat_table);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        View blankHeader;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            blankHeader = itemView.findViewById(R.id.blank_view);
        }
    }

    private static class StatusCardViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView infected, recovered, death, country;

        StatusCardViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.country_name);
            infected = itemView.findViewById(R.id.infected);
            recovered = itemView.findViewById(R.id.recovered);
            death = itemView.findViewById(R.id.death);
        }
    }

}
