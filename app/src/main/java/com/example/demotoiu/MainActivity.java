package com.example.demotoiu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Debug;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.example.demotoiu.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private HandlerThread handlerThread;
    private Handler handler;
    private ActivityMainBinding mainBinding;

    private double startBatteryLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        mainBinding.btnTestArrayMap.setOnClickListener(v -> {
            handlerThread = new HandlerThread("PerformanceHandlerThread");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());

            // Lấy mức pin ban đầu
            startBatteryLevel = getBatteryLevel();

            handler.post(() -> {
                //Todo: Thực hiện công việc đo CPU Usage và bộ nhớ sử dụng
                PerformanceResult result = measurePerformance(() -> {
                    ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
                    // Thêm một lượng lớn dữ liệu vào ArrayMap
                    for (int i = 0; i < 10000; i++) {
                        arrayMap.put("Key" + i, i);
                    }
                    // Truy cập và in ra một số giá trị
                    for (int i = 0; i < 1000; i++) {
                        int randomIndex = (int) (Math.random() * arrayMap.size());
                        String randomKey = arrayMap.keyAt(randomIndex);
                        int randomValue = arrayMap.valueAt(randomIndex);
                        System.out.println("Value for key '" + randomKey + "' in ArrayMap: " + randomValue);
                    }
                // Tính tổng của tất cả các giá trị trong ArrayMap
                    int sum = 0;
                    for (int i = 0; i < arrayMap.size(); i++) {
                        sum += arrayMap.valueAt(i);
                    }

                    System.out.println("Sum of all values in ArrayMap: " + sum);
                });

                runOnUiThread(() -> {
                    mainBinding.textViewCpuArrayMap.setText("CPU Usage: " + result.cpuUsage + "%");
                    mainBinding.textViewMemoryArrayMap.setText("Memory Usage: " + result.memoryUsage + " KB");
                    mainBinding.textViewEnergyArrayMap.setText("Energy Usage: " + calculateEnergyUsage() + " mWh");
                });

                handlerThread.quit();
            });
        });

        mainBinding.btnTestHashMap.setOnClickListener(v -> {
            handlerThread = new HandlerThread("PerformanceHandlerThread");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());

            // Lấy mức pin ban đầu
            startBatteryLevel = getBatteryLevel();

            handler.post(() -> {
                //Todo: Thực hiện công việc đo CPU Usage và bộ nhớ sử dụng
                PerformanceResult result = measurePerformance(() -> {
                    HashMap<String, Integer> hashMap = new HashMap<>();

                    // Thêm một lượng lớn dữ liệu vào HashMap
                    for (int i = 0; i < 10000; i++) {
                        hashMap.put("Key" + i, i);
                    }
                    // Truy cập và in ra một số giá trị
                    for (int i = 0; i < 1000; i++) {
                        int randomIndex = (int) (Math.random() * hashMap.size());
                        String randomKey = (String) hashMap.keySet().toArray()[randomIndex];
                        int randomValue = hashMap.get(randomKey);
                        System.out.println("Value for key '" + randomKey + "' in HashMap: " + randomValue);
                    }
                    // Tính tổng của tất cả các giá trị trong HashMap
                    int sum = 0;
                    for (int value : hashMap.values()) {
                        sum += value;
                    }

                    System.out.println("Sum of all values in HashMap: " + sum);
                });

                runOnUiThread(() -> {
                    mainBinding.textViewCpuHashMap.setText("CPU Usage: " + result.cpuUsage + "%");
                    mainBinding.textViewMemoryHashMap.setText("Memory Usage: " + result.memoryUsage + " KB");
                    mainBinding.textViewEnergyHashMap.setText("Energy Usage: " + calculateEnergyUsage() + " mWh");
                });

                handlerThread.quit();
            });
        });

        mainBinding.btnTestSparseArray.setOnClickListener(view1 -> {
            handlerThread = new HandlerThread("PerformanceHandlerThread");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());

            // Lấy mức pin ban đầu
            startBatteryLevel = getBatteryLevel();

            handler.post(() -> {
                //Todo: Thực hiện công việc đo CPU Usage và bộ nhớ sử dụng
                PerformanceResult result = measurePerformance(() -> {
                    SparseArray<Integer> sparseArray = new SparseArray<>();
                    // Thêm một lượng lớn dữ liệu vào ArrayMap
                    for (int i = 0; i < 10000; i++) {
                        sparseArray.put(i, i);
                    }
                    // Truy cập và in ra một số giá trị
                    for (int i = 0; i < 1000; i++) {
                        int randomIndex = (int) (Math.random() * sparseArray.size());
                        int randomKey = sparseArray.keyAt(randomIndex);
                        int randomValue = sparseArray.valueAt(randomIndex);
                        System.out.println("Value for key '" + randomKey + "' in ArrayMap: " + randomValue);
                    }
                    // Tính tổng của tất cả các giá trị trong ArrayMap
                    int sum = 0;
                    for (int i = 0; i < sparseArray.size(); i++) {
                        sum += sparseArray.valueAt(i);
                    }

                    System.out.println("Sum of all values in ArrayMap: " + sum);
                });

                runOnUiThread(() -> {
                    mainBinding.textViewCpuSparseArray.setText("CPU Usage: " + result.cpuUsage + "%");
                    mainBinding.textViewMemorySparseArray.setText("Memory Usage: " + result.memoryUsage + " KB");
                    mainBinding.textViewEnergySparseArray.setText("Energy Usage: " + calculateEnergyUsage() + " mWh");
                });

                handlerThread.quit();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // Kiểm tra xem quyền đã được cấp hay chưa
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, thực hiện công việc liên quan đến đo lường điện năng
                getBatteryLevel();
            } else {
                // Quyền không được cấp, có thể thông báo cho người dùng hoặc thực hiện xử lý khác
                Toast.makeText(this, "Quyền truy cập không được cấp.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private PerformanceResult measurePerformance(Runnable task) {
        Debug.startMethodTracing("cpu_trace"); // Bắt đầu ghi dữ liệu hiệu năng CPU

        long startCpuTime = Debug.threadCpuTimeNanos();
        long startMemory = getUsedMemory();

        task.run();

        long endCpuTime = Debug.threadCpuTimeNanos();
        long endMemory = getUsedMemory();

        Debug.stopMethodTracing(); // Kết thúc ghi dữ liệu hiệu năng CPU

        long elapsedTime = endCpuTime - startCpuTime;
        long elapsedRealTime = System.nanoTime();

        // Tính toán CPU usage dựa trên thời gian CPU hoạt động và thời gian thực tế
        double cpuUsage = (elapsedTime / (double) elapsedRealTime) * 100;

        // Tính toán bộ nhớ sử dụng dựa trên sự khác biệt giữa bộ nhớ trước và sau khi thực hiện công việc
        long memoryUsage = endMemory - startMemory;

        return new PerformanceResult(cpuUsage, memoryUsage);
    }

    private long getUsedMemory() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem - memoryInfo.availMem;
    }
    private double getBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return (level / (double) scale) * 100.0;
    }

    private double calculateEnergyUsage() {
        double currentBatteryLevel =  getBatteryLevel();
        double energyConsumed = (startBatteryLevel - currentBatteryLevel);

        // Nếu có thêm thông số về pin như điện thế, bạn có thể tính toán năng lượng tiêu thụ chính xác hơn
        return energyConsumed;
    }

    private static class PerformanceResult {
        private final double cpuUsage;
        private final long memoryUsage;

        public PerformanceResult(double cpuUsage, long memoryUsage) {
            this.cpuUsage = cpuUsage;
            this.memoryUsage = memoryUsage;
        }
    }
}
