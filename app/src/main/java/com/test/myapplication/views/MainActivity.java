package com.test.myapplication.views;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.test.myapplication.R;
import com.test.myapplication.adadpter.ThumbnailAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1000;
    private static final int SELECTED_REQUEST = 100;
    private static final int IMAGE_PICKER_SELECT = 101;
    private static final int PICKFILE_REQUEST_CODE = 102;
    private static final int CAMERA_REQUEST = 103;
    private static final int MY_CAMERA_PERMISSION_CODE = 104;
    private static final int REQUEST_VIDEO_CAPTURE = 105;
    private EditText etPostTitle;
    private TextView tvPostTitleCountDown;
    private EditText etPosDescription;
    private TextView tvPosDescriptionCountDown;
    private EditText etSelectPostCategory;
    private ImageButton ivGridCategory;
    private EditText etRate;
    private EditText etPaymentMethod;
    private EditText etStartDate;
    private EditText etJobTerm;
    private EditText etpostLocation;
    private EditText etBudget;
    private EditText etINR;
    private AlertDialog.Builder builder;
    private ImageButton ivPostAttachments;
    private ArrayList<Uri> uriList = new ArrayList<>();
    private ThumbnailAdapter thumbnailAdapter;
    private RecyclerView rvThumbnails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        setTitle("Post ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing views
        etPostTitle = findViewById(R.id.etPostTitle);
        tvPostTitleCountDown = findViewById(R.id.tvCountDown);
        etPosDescription = findViewById(R.id.etPostDescription);
        tvPosDescriptionCountDown = findViewById(R.id.tvPostDescription);
        etSelectPostCategory = findViewById(R.id.etSelectPostCategory);
        ivGridCategory = findViewById(R.id.ivGridCategory);
        etINR = findViewById(R.id.etINr);
        etBudget = findViewById(R.id.etBudget);
        etJobTerm = findViewById(R.id.etJobTerms);
        etStartDate = findViewById(R.id.etStartDate);
        etpostLocation = findViewById(R.id.etPostLocation);
        etPaymentMethod = findViewById(R.id.etPaymentMethod);
        etRate = findViewById(R.id.etRate);
        ivPostAttachments = findViewById(R.id.ivPostAttachments);
        rvThumbnails = findViewById(R.id.rvThumbnails);
        //Setting focus change listeners
        etJobTerm.setOnFocusChangeListener(this);
        etPaymentMethod.setOnFocusChangeListener(this);
        etStartDate.setOnFocusChangeListener(this);
        etpostLocation.setOnFocusChangeListener(this);
        etRate.setOnFocusChangeListener(this);
        etPostTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.hasFocus()) {
                    tvPostTitleCountDown.setVisibility(View.VISIBLE);
                } else {
                    tvPostTitleCountDown.setVisibility(View.GONE);
                }
            }
        });
        etPosDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.hasFocus()) {
                    tvPosDescriptionCountDown.setVisibility(View.VISIBLE);
                } else {
                    tvPosDescriptionCountDown.setVisibility(View.GONE);
                }
            }
        });

        //Text watchers for the title and descrtiption to show text limits
        etPosDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvPosDescriptionCountDown.setText((400 - editable.toString().length()) + " " + getString(R.string.characters_left));
            }
        });
        etPostTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvPostTitleCountDown.setText((50 - editable.toString().length()) + " " + getString(R.string.characters_left));
            }
        });


        ivGridCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, GridActivity.class), 100);
            }
        });

        ivPostAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomsheet();
            }
        });
        thumbnailAdapter = new ThumbnailAdapter(uriList, this);
        rvThumbnails.setAdapter(thumbnailAdapter);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.hasFocus()) {
            switch (view.getId()) {
                case R.id.etRate: {
                    final CharSequence[] items = {"No Preference", "Fixed Budget", "Hourly rate"};
                    showSingleChoiceDialog(items, "Rate", (EditText) view);
                }
                break;
                case R.id.etPaymentMethod: {
                    final CharSequence[] items = {"No Preference", "E-Payment", "Cash"};
                    showSingleChoiceDialog(items, "Payment Method", (EditText) view);
                }
                break;
                case R.id.etJobTerms: {
                    final CharSequence[] items = {"No Preference", "Fixed Budget", "Hourly rate"};
                    showSingleChoiceDialog(items, "Job Terms", (EditText) view);
                }
                break;
                case R.id.etStartDate: {
                    launchDatePicker();
                }
                break;
                case R.id.etPostLocation: {
                    launchPlacesIntent();
                }
                break;
                case R.id.etPostTitle: {
                    if (view.hasFocus()) {
                        tvPostTitleCountDown.setVisibility(View.VISIBLE);
                    } else {
                        tvPostTitleCountDown.setVisibility(View.GONE);
                    }
                }
                break;
            }
        }
    }

    private void launchDatePicker() {
        Calendar mCalendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.AppThemeDialogStyle,
                this,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    private void showSingleChoiceDialog(final CharSequence[] items, String title, final EditText view) {
        // final CharSequence[] items = {"Option-1", "Option-2", "Option-3", "Option-4"};
        builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                view.setText(items[item]);
            }
        });

        builder.setPositiveButton("SELECT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        view.setText("");
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECTED_REQUEST) {
                etSelectPostCategory.setText(data.getIntExtra("count", 0) + " " + "Categories Selected");
            } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                etpostLocation.setText(place.getAddress().toString());
            } else if (requestCode == IMAGE_PICKER_SELECT) {
                updateList(data);
            } else if (requestCode == PICKFILE_REQUEST_CODE) {
                updateList(data);
            } else if (requestCode == CAMERA_REQUEST) {
                updateList(data);
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                updateList(data);
            }

        }
    }

    private void updateList(Intent data) {
        uriList.add(0, data.getData());
        thumbnailAdapter.updateItems(uriList);
        thumbnailAdapter.notifyItemInserted(0);
        rvThumbnails.smoothScrollToPosition(0);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String myFormat = "EEE MMM yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etStartDate.setText(sdf.format(calendar.getTime()));

    }

    public void launchPlacesIntent() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(this);
            this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException ex) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException ex) {
            // TODO: Handle the error.
        }
    }


    public void showBottomsheet() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        initBottomSheet(view, bottomSheetDialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void initBottomSheet(View view, final BottomSheetDialog bottomSheetDialog) {
        LinearLayout llCaptureCamera = view.findViewById(R.id.llCaptureCamera);
        LinearLayout llCapturePhotoVideo = view.findViewById(R.id.llCapturePhotoVideo);
        LinearLayout llCapturePhotoLibrary = view.findViewById(R.id.llCapturePhotoLibrary);
        LinearLayout llCaptureDocument = view.findViewById(R.id.llCaptureDocument);
        llCaptureCamera.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
                bottomSheetDialog.dismiss();
            }
        });
        llCapturePhotoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
                bottomSheetDialog.dismiss();
            }
        });
        llCapturePhotoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/* video/*");
                startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
                bottomSheetDialog.dismiss();
            }
        });
        llCaptureDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
                bottomSheetDialog.dismiss();
            }
        });


    }


}
