package tyxo.mobilesafe.utils.bitmap;

import android.content.Intent;
import android.net.Uri;

/**
 * Created with Android Studio.
 * Date: 10/1/14
 * Time: 11:00 AM
 * Desc:
 * Revision:
 * - 10:20 2014/10/01 The basic interfaces.
 * - 13:00 2014/10/03 Able to get access to the CropParams the related Context.
 * - 23:00 2015/09/05 Remove getContext, rename onCancel, onFailed, add handleIntent.
 */
public interface CropHandler {

    void onPhotoCropped(Uri uri);

    void onCompressed(Uri uri);

    void onCancel();

    void onFailed(String message);

    void handleIntent(Intent intent, int requestCode);

    CropParams getCropParams();
}
