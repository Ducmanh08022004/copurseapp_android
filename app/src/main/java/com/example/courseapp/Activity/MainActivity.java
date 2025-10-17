package com.example.courseapp.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.courseapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Thiết lập sự kiện lắng nghe khi một mục trên thanh điều hướng được chọn
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_discover) {
                // Fragment để xem tất cả khóa học
                selectedFragment = new CoursesFragment();
            } else if (itemId == R.id.nav_my_courses) {
                // Fragment để xem các khóa học đã đăng ký và tiến trình
                selectedFragment = new MyCoursesFragment(); // Chúng ta sẽ tạo Fragment này
            } else if (itemId == R.id.nav_profile) {
                // Fragment để xem profile và đăng xuất
                selectedFragment = new ProfileFragment();
            }

            // Nếu có fragment được chọn, tải nó lên FrameLayout
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        // Tải Fragment mặc định (Khám phá) khi Activity được tạo lần đầu
        if (savedInstanceState == null) {
            loadFragment(new CoursesFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
