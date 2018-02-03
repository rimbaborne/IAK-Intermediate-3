package iak.rimbaborne.intermediate3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView txtJudul;
    private TextView txtDeskripsi;
    private ImageView imgMovie;
    private TextView txtVote;
    public TextView txtTahun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtJudul = (TextView) findViewById(R.id.txtJudul);
        txtDeskripsi = (TextView) findViewById(R.id.txtDeskripsi);
        imgMovie = (ImageView) findViewById(R.id.imgMovie);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        imgMovie.setScaleType(ImageView.ScaleType.CENTER_CROP);
        txtVote = (TextView) findViewById(R.id.txtVote);

        String latar = getIntent().getStringExtra("latar");

        String jud = getIntent().getStringExtra("judul");
        String des = getIntent().getStringExtra("deskripsi");
        String thn = getIntent().getStringExtra("tahun");
        String vote = getIntent().getStringExtra("vote");
        String id_film = getIntent().getStringExtra("id");


        Picasso.with(this).load(latar).placeholder(R.drawable.logo_item).into(imgMovie);

        txtJudul.setText(jud);
        txtDeskripsi.setText(des);
        txtTahun.setText("Release date : " + thn);
        txtVote.setText("Rating : " + vote + " / 10");
    }

}
