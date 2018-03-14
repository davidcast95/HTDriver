package huang.android.logistic_driver.GPSActivity;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by davidwibisono on 2/6/18.
 */

public class PhoneSignalHelper extends PhoneStateListener {
    int mSignalStrength = 0;
    TelephonyManager mTelephoneManager;

    public PhoneSignalHelper(Context context) {
        mTelephoneManager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        mTelephoneManager.listen(this, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        mSignalStrength = signalStrength.getGsmSignalStrength();
        mSignalStrength = (2 * mSignalStrength) - 113;
    }

    public int getCurrentSignal() {
        return mSignalStrength;
    }
}