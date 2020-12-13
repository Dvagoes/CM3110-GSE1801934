package com.example.cm3110_gse1801934;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViewTimesAdapter extends RecyclerView.Adapter<ViewTimesAdapter.TimeItemHolder>{

    private Context context;
    private List<PassTime> times;


    public ViewTimesAdapter(Context context, List<PassTime> times) {
        super();
        this.context = context;
        this.times = times;
    }

    @NonNull
    @Override
    public TimeItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View timeView = LayoutInflater.from(context)
                .inflate(R.layout.time_list_layout, parent, false);
        TimeItemHolder viewHolder = new TimeItemHolder(timeView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeItemHolder holder, int position) {
        PassTime time = this.times.get(position);

        TextView tv_time = holder.timeItemView
                .findViewById(R.id.timeLabel);
        tv_time.setText(time.getStartTime().toString());

    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    class TimeItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View timeItemView;
        private ViewTimesAdapter adapter;

        public TimeItemHolder(View timeItemView, ViewTimesAdapter adapter) {
            super(timeItemView);
            this.timeItemView = timeItemView;
            this.adapter = adapter;
            this.timeItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {



            int position = getAdapterPosition();

            PassTime time = times.get(position);

            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
            intent.putExtra(AlarmClock.EXTRA_MESSAGE, "ISS Passing");
            intent.putExtra(AlarmClock.EXTRA_HOUR, time.getStartTime().getHour());
            intent.putExtra(AlarmClock.EXTRA_MINUTES, time.getStartTime().getMinute());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }

    }

}

