package ethiopia.covid.android.ui.fragment;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.QuestionItem;
import ethiopia.covid.android.data.QuestionnaireItem;
import ethiopia.covid.android.data.Result;
import ethiopia.covid.android.ui.activity.MainActivity;
import ethiopia.covid.android.ui.adapter.ResultRecyclerAdapter;
import timber.log.Timber;

import static ethiopia.covid.android.util.Constant.PREFERENCE_QTIME;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class ResultFragment extends BaseFragment {

    private Map<QuestionnaireItem, List<QuestionItem>> questionItems;
    private String hashOfQuestionnaire;
    private RecyclerView recyclerView;
    private Location currentLocation;
    private MaterialButton sendButton;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private ResultFragment(Map<QuestionnaireItem, List<QuestionItem>> questionItems, String hashOfQuestionnaire) {
        this.questionItems = questionItems;
        this.hashOfQuestionnaire = hashOfQuestionnaire;
    }

    static ResultFragment newInstance(Map<QuestionnaireItem, List<QuestionItem>> items , String hashOfQuestionnaire) {
        Bundle args = new Bundle();
        ResultFragment fragment = new ResultFragment(items, hashOfQuestionnaire);
        fragment.setArguments(args);
        return fragment;
    }

    void setMap(Map<QuestionnaireItem, List<QuestionItem>> items) {
        questionItems = items;
        recyclerView.setAdapter(new ResultRecyclerAdapter(questionItems));
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void handleWindowInsets(View view) {
        view.setOnApplyWindowInsetsListener((v1, insets) -> {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            marginParams.setMargins( 0, insets.getSystemWindowInsetTop(), 0, 0);
            view.setLayoutParams(marginParams);

            return insets;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            recyclerView.requestApplyInsets();
            handleWindowInsets(recyclerView);
        }
    }

    public void setHashOfQuestionnaire(String hashOfQuestionnaire) {
        this.hashOfQuestionnaire = hashOfQuestionnaire;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.result_fragment, container, false);
        recyclerView = mainView.findViewById(R.id.result_recycler);
        sendButton = mainView.findViewById(R.id.questionnaire_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.VERTICAL , false));
        recyclerView.setAdapter(new ResultRecyclerAdapter(questionItems));

        mainView.findViewById(R.id.back_button).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).callBackOnParentFragment();
        });

        sendButton.setOnClickListener(v -> {
            Result result = new Result(
                    questionItems,
                    currentLocation != null ? currentLocation.getLongitude() : 0,
                    currentLocation != null ? currentLocation.getLatitude() : 0,
                    currentLocation != null ? currentLocation.getAccuracy() : 0
            );

            String resultJson = new GsonBuilder().enableComplexMapKeySerialization()
                    .addSerializationExclusionStrategy(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes field) {
                            return field.getDeclaringClass() == QuestionnaireItem.class &&
                                    field.getName().equals("questionItems") ||
                                    field.getDeclaringClass() == QuestionItem.class &&
                                            field.getName().equals("questionIconResource") ||
                                    field.getDeclaringClass() == QuestionItem.class &&
                                            field.getName().equals("selectedQuestion") ||
                                    field.getDeclaringClass() == QuestionItem.class &&
                                            field.getName().equals("questionIconLink");
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create().toJson(result);

            Map<String , Object> answer = new HashMap<>();
            answer.put(hashOfQuestionnaire , resultJson);

            firestore
                    .collection("answers")
                    .add(answer)
                    .addOnSuccessListener(r -> Timber.d("Added successfully.") );

            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                    .putLong(PREFERENCE_QTIME , new Date().getTime()).apply();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).forcefulOnBackPressed();
            }

        });
        return mainView;
    }

}
