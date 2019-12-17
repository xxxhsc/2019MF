package activityest.com.example.hsc.a2019mf.view.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import activityest.com.example.hsc.a2019mf.R;

public class Fragment_textnews extends Fragment {


    public Fragment_textnews() {
    }


    public static Fragment_textnews newInstance() {
        Fragment_textnews fragment = new Fragment_textnews();
        return fragment;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_article, container, false);
    }

}
