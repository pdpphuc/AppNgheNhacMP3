package com.phuphuc.appnghenhacmp3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtStart, txtEnd;
    ImageButton btnPrev, btnPlay, btnStop, btnNext;
    SeekBar skTime;
    List<Song> listSong;
    int position;
    MediaPlayer mediaPlayer;
    SimpleDateFormat dinhDangThoiGian;
    Handler handler;
    ImageView imgHinh;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = AnimationUtils.loadAnimation(this, R.anim.cd_rotate);
        dinhDangThoiGian = new SimpleDateFormat("mm:ss");
        AnhXa();
        TaoDanhMucBaiHat();
        khoiTaoMediaPlayer();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
//                    animation.cancel();
                    btnPlay.setImageResource(R.drawable.play);
                }
                else {
                    mediaPlayer.start();
                    capNhatThoiGianHienTai();
                    imgHinh.startAnimation(animation);
                    btnPlay.setImageResource(R.drawable.pause);
                }
                hienThiTongThoiGian();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
//                imgHinh.clearAnimation();
                btnPlay.setImageResource(R.drawable.play);
                khoiTaoMediaPlayer();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                --position;
                if (position < 0) {
                    position = listSong.size() - 1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    imgHinh.clearAnimation();
                    mediaPlayer.release();
                }
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                imgHinh.startAnimation(animation);
                capNhatThoiGianHienTai();
                btnPlay.setImageResource(R.drawable.pause);
                hienThiTongThoiGian();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiBaiKeTiep();
            }
        });

        skTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skTime.getProgress());
            }
        });
    }

    private void AnhXa() {
        txtTitle = (TextView) findViewById(R.id.textviewTitle);
        txtStart =  (TextView) findViewById(R.id.textviewStart);
        txtEnd = (TextView) findViewById(R.id.textviewEnd);
        btnPrev = (ImageButton) findViewById(R.id.imagebuttonPrevious);
        btnPlay = (ImageButton) findViewById(R.id.imagebuttonPlay);
        btnStop = (ImageButton) findViewById(R.id.imagebuttonStop);
        btnNext = (ImageButton) findViewById(R.id.imagebuttonNext);
        skTime = (SeekBar) findViewById(R.id.seekbarTime);
        imgHinh = (ImageView) findViewById(R.id.imageViewCD);
    }

    private void khoiTaoMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, listSong.get(position).getFile());
        txtTitle.setText(listSong.get(position).getTitle());
    }

    private void TaoDanhMucBaiHat() {
        listSong = new ArrayList<>();
        listSong.add(new Song("OneRepublic - Counting Stars", R.raw.counting_stars));
        listSong.add(new Song("Gurenge - Demon Slayer (Opening)", R.raw.demon_slayer_opening));
        listSong.add(new Song("DAOKO × 米津玄師『打上花火』", R.raw.firework));
        listSong.add(new Song("Legends Never Die (ft. Against The Current)", R.raw.legends_never_die));
        listSong.add(new Song("BÍCH PHƯƠNG - Mình Yêu Nhau Đi", R.raw.minh_yeu_nhau_di));
        listSong.add(new Song("Vicetone - Nevada (feat. Cozi Zuehlsdorff)", R.raw.nevada));
        listSong.add(new Song("Ed Sheeran - Shape Of You ( cover by J.Fla )", R.raw.shape_of_you_cover));
    }

    private void hienThiTongThoiGian() {
        txtEnd.setText(dinhDangThoiGian.format(mediaPlayer.getDuration()));
        skTime.setMax(mediaPlayer.getDuration());
    }

    private void capNhatThoiGianHienTai() {
        if (handler == null) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtStart.setText(dinhDangThoiGian.format(mediaPlayer.getCurrentPosition()));
                    skTime.setProgress(mediaPlayer.getCurrentPosition());
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            choiBaiKeTiep();
                        }
                    });
                    handler.postDelayed(this, 500);
                }
            }, 100);
        }
    }

    private void choiBaiKeTiep() {
        ++position;
        if (position > listSong.size() - 1) {
            position = 0;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
//            imgHinh.clearAnimation();
        }
        khoiTaoMediaPlayer();
        mediaPlayer.start();
        imgHinh.startAnimation(animation);
        capNhatThoiGianHienTai();
        btnPlay.setImageResource(R.drawable.pause);
        hienThiTongThoiGian();
    }
}