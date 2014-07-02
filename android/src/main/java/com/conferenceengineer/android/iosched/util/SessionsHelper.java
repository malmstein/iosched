/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.conferenceengineer.android.iosched.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.MenuItem;
import com.conferenceengineer.android.iosched.appwidget.ScheduleWidgetProvider;
import com.conferenceengineer.android.iosched.conference857.R;
import com.conferenceengineer.android.iosched.provider.ScheduleContract;
import com.conferenceengineer.android.iosched.service.ScheduleUpdaterService;
import com.conferenceengineer.android.iosched.ui.SocialStreamActivity;
import com.conferenceengineer.android.iosched.ui.SocialStreamFragment;

import static com.conferenceengineer.android.iosched.util.LogUtils.LOGD;
import static com.conferenceengineer.android.iosched.util.LogUtils.makeLogTag;

/**
 * Helper class for dealing with common actions to take on a session.
 */
public final class SessionsHelper {

    private static final String TAG = makeLogTag(SessionsHelper.class);

    private final Activity mActivity;

    public SessionsHelper(Activity activity) {
        mActivity = activity;
    }

    public Intent createShareIntent(int messageTemplateResId, String title, String hashtags,
            String url) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(mActivity)
                .setType("text/plain")
                .setText(mActivity.getString(messageTemplateResId,
                        title, UIUtils.getSessionHashtagsString(hashtags), url));
        return builder.getIntent();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void tryConfigureShareMenuItem(MenuItem menuItem, int messageTemplateResId,
            final String title, String hashtags, String url) {
        if (UIUtils.hasICS()) {
            ActionProvider itemProvider = MenuItemCompat.getActionProvider(menuItem);
            ShareActionProvider provider;
            if (!(itemProvider instanceof ShareActionProvider)) {
                provider = new ShareActionProvider(mActivity);
            } else {
                provider = (ShareActionProvider) itemProvider;
            }
            provider.setShareIntent(createShareIntent(messageTemplateResId, title, hashtags, url));
            provider.setOnShareTargetSelectedListener(
                    new ShareActionProvider.OnShareTargetSelectedListener() {
                        @Override
                        public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                            LOGD("Tracker", "Shared: " + title);
                            return false;
                        }
                    });

            MenuItemCompat.setActionProvider(menuItem, provider);
        }
    }

    public void shareSession(Context context, int messageTemplateResId, String title,
            String hashtags, String url) {
        LOGD("Tracker", "Shared: " + title);
        context.startActivity(Intent.createChooser(
                createShareIntent(messageTemplateResId, title, hashtags, url),
                context.getString(R.string.title_share)));
    }

    public void setSessionStarred(Uri sessionUri, boolean starred, String title) {
        LOGD(TAG, "setSessionStarred uri=" + sessionUri + " starred=" +
                starred + " title=" + title);
        sessionUri = ScheduleContract.addCallerIsSyncAdapterParameter(sessionUri);
        final ContentValues values = new ContentValues();
        values.put(ScheduleContract.Sessions.SESSION_STARRED, starred);
        AsyncQueryHandler handler =
                new AsyncQueryHandler(mActivity.getContentResolver()) {
                };
        handler.startUpdate(-1, null, sessionUri, values, null, null);

        // Because change listener is set to null during initialization, these
        // won't fire on pageview.
        mActivity.sendBroadcast(ScheduleWidgetProvider.getRefreshBroadcastIntent(mActivity, false));

        // Sync to the cloudz.
        uploadStarredSession(mActivity, sessionUri, starred);
    }

    public static void uploadStarredSession(Context context, Uri sessionUri, boolean starred) {
        final Intent updateServerIntent = new Intent(context, ScheduleUpdaterService.class);
        updateServerIntent.putExtra(ScheduleUpdaterService.EXTRA_SESSION_ID,
                ScheduleContract.Sessions.getSessionId(sessionUri));
        updateServerIntent.putExtra(ScheduleUpdaterService.EXTRA_IN_SCHEDULE, starred);
        context.startService(updateServerIntent);
    }

    public void startSocialStream(String hashtags) {
        Intent intent = new Intent(mActivity, SocialStreamActivity.class);
        intent.putExtra(SocialStreamFragment.EXTRA_QUERY, UIUtils.getSessionHashtagsString(hashtags));
        mActivity.startActivity(intent);
    }
}
