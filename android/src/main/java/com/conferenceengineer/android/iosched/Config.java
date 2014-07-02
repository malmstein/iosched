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

package com.conferenceengineer.android.iosched;

import android.net.Uri;
import com.conferenceengineer.android.iosched.util.ParserUtils;

import java.util.TimeZone;

public class Config {
    // Log tag
    public static final String LOG_TAG = "DCMadrid2014";

    // General configuration
    public static final int CONFERENCE_YEAR = 2014;

    // Conference times
    public static final TimeZone CONFERENCE_TIME_ZONE = TimeZone.getTimeZone("Europe/Madrid");
    public static final String CONFERENCE_TIME_ZONE_OFFSET = "+01:00";

    public static final long CONFERENCE_START_MILLIS = ParserUtils.parseTime("2014-07-03T16:00:00.000" + CONFERENCE_TIME_ZONE_OFFSET);
    public static final long CONFERENCE_END_MILLIS = ParserUtils.parseTime("2014-07-05T17:30:00.000" + CONFERENCE_TIME_ZONE_OFFSET);

    // The base URL for event data
    public static final String DATA_ROOT = "http://updates.conferenceengineer.com/";

    // Used in {@link UIUtils#tryTranslateHttpIntent(android.app.Activity)}.
    public static final Uri SESSION_DETAIL_WEB_URL_PREFIX = Uri.parse("http://es.droidcon.com/2014/programa/");

    // Feedback URL
    public static final String FEEDBACK_URL = "http://es.droidcon.com/2014/programa/";

    // OAuth 2.0 related config
    public static final String APP_NAME = "DroidconNL2013-Android";

    // Conference API-specific config
    public static final String EVENT_ID = "857";
    public static final String CONFERENCE_IMAGE_PREFIX_URL = "http://es.droidcon.com/2014/wp-content/uploads/";

    // Conference public WiFi AP parameters
    public static final String WIFI_SSID = "droidcon";
    public static final String WIFI_PASSPHRASE = "wifi";

    // Conference hashtag
    public static final String CONFERENCE_HASHTAG = "#droidcones";

    // GCM config
    // TODO: Add your GCM information here.
    public static final String GCM_SERVER_URL = "https://YOUR_GCM_APP_ID_HERE.appspot.com";
    public static final String GCM_SENDER_ID = "YOUR_GCM_SENDER_ID_HERE";
    public static final String GCM_API_KEY = "YOUR_GCM_API_KEY_HERE";
}
