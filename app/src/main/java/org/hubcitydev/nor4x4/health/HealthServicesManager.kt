package org.hubcitydev.nor4x4.health

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HealthServicesManager(context: Context) {
    private val healthClient = HealthServices.getClient(context)
    private val measureClient = healthClient.measureClient

    fun heartRateMeasureFlow(): Flow<Double> = callbackFlow {
        val callback = object : MeasureCallback {
            override fun onAvailabilityChanged(dataType: DeltaDataType<*, *>, availability: Availability) {
                if (availability is DataTypeAvailability) {
                    Log.d("HealthServices", "Availability changed: $availability")
                }
            }

            override fun onDataReceived(data: DataPointContainer) {
                val heartRateDataPoints = data.getData(DataType.HEART_RATE_BPM)
                heartRateDataPoints.lastOrNull()?.value?.let { hr ->
                    trySend(hr)
                }
            }
        }

        try {
            measureClient.registerMeasureCallback(DataType.HEART_RATE_BPM, callback)
        } catch (e: Exception) {
            Log.e("HealthServices", "Error registering measure callback", e)
        }

        awaitClose {
            try {
                measureClient.unregisterMeasureCallbackAsync(DataType.HEART_RATE_BPM, callback)
            } catch (e: Exception) {
                Log.e("HealthServices", "Error unregistering measure callback", e)
            }
        }
    }
}
