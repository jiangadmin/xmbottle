/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.utils;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseEmojiconInfoProvider;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.model.EaseDefaultEmojiconDatas;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EaseSmileUtils {
    public static final String DELETE_KEY = "em_delete_delete_expression";

    public static final String ee_00 = "[ee_00]";
    public static final String ee_01 = "[ee_01]";
    public static final String ee_02 = "[ee_02]";
    public static final String ee_03 = "[ee_03]";
    public static final String ee_04 = "[ee_04]";
    public static final String ee_05 = "[ee_05]";
    public static final String ee_06 = "[ee_06]";
    public static final String ee_07 = "[ee_07]";
    public static final String ee_08 = "[ee_08]";
    public static final String ee_09 = "[ee_09]";

    public static final String ee_10 = "[ee_10]";
    public static final String ee_11 = "[ee_11]";
    public static final String ee_12 = "[ee_12]";
    public static final String ee_13 = "[ee_13]";
    public static final String ee_14 = "[ee_14]";
    public static final String ee_15 = "[ee_15]";
    public static final String ee_16 = "[ee_16]";
    public static final String ee_17 = "[ee_17]";
    public static final String ee_18 = "[ee_18]";
    public static final String ee_19 = "[ee_19]";

    public static final String ee_20 = "[ee_20]";
    public static final String ee_21 = "[ee_21]";
    public static final String ee_22 = "[ee_22]";
    public static final String ee_23 = "[ee_23]";
    public static final String ee_24 = "[ee_24]";
    public static final String ee_25 = "[ee_25]";
    public static final String ee_26 = "[ee_26]";
    public static final String ee_27 = "[ee_27]";
    public static final String ee_28 = "[ee_28]";
    public static final String ee_29 = "[ee_29]";

    public static final String ee_30 = "[ee_30]";
    public static final String ee_31 = "[ee_31]";
    public static final String ee_32 = "[ee_32]";
    public static final String ee_33 = "[ee_33]";
    public static final String ee_34 = "[ee_34]";
    public static final String ee_35 = "[ee_35]";
    public static final String ee_36 = "[ee_36]";
    public static final String ee_37 = "[ee_37]";
    public static final String ee_38 = "[ee_38]";
    public static final String ee_39 = "[ee_39]";

    public static final String ee_40 = "[ee_40]";
    public static final String ee_41 = "[ee_41]";
    public static final String ee_42 = "[ee_42]";
    public static final String ee_43 = "[ee_43]";
    public static final String ee_44 = "[ee_44]";
    public static final String ee_45 = "[ee_45]";
    public static final String ee_46 = "[ee_46]";
    public static final String ee_47 = "[ee_47]";
    public static final String ee_48 = "[ee_48]";
    public static final String ee_49 = "[ee_49]";

    public static final String ee_50 = "[ee_50]";
    public static final String ee_51 = "[ee_51]";
    public static final String ee_52 = "[ee_52]";
    public static final String ee_53 = "[ee_53]";
    public static final String ee_54 = "[ee_54]";
    public static final String ee_55 = "[ee_55]";
    public static final String ee_56 = "[ee_56]";
    public static final String ee_57 = "[ee_57]";
    public static final String ee_58 = "[ee_58]";
    public static final String ee_59 = "[ee_59]";

    public static final String ee_60 = "[ee_60]";
    public static final String ee_61 = "[ee_61]";
    public static final String ee_62 = "[ee_62]";
    public static final String ee_63 = "[ee_63]";
    public static final String ee_64 = "[ee_64]";
    public static final String ee_65 = "[ee_65]";
    public static final String ee_66 = "[ee_66]";
    public static final String ee_67 = "[ee_67]";
    public static final String ee_68 = "[ee_68]";
    public static final String ee_69 = "[ee_69]";

    public static final String ee_70 = "[ee_70]";
    public static final String ee_71 = "[ee_71]";
    public static final String ee_72 = "[ee_72]";
    public static final String ee_73 = "[ee_73]";
    public static final String ee_74 = "[ee_74]";
    public static final String ee_75 = "[ee_75]";
    public static final String ee_76 = "[ee_76]";
    public static final String ee_77 = "[ee_77]";
    public static final String ee_78 = "[ee_78]";
    public static final String ee_79 = "[ee_79]";

    public static final String ee_80 = "[ee_80]";
    public static final String ee_81 = "[ee_81]";
    public static final String ee_82 = "[ee_82]";
    public static final String ee_83 = "[ee_83]";
    public static final String ee_84 = "[ee_84]";
    public static final String ee_85 = "[ee_85]";
    public static final String ee_86 = "[ee_86]";
    public static final String ee_87 = "[ee_87]";
    public static final String ee_88 = "[ee_88]";
    public static final String ee_89 = "[ee_89]";

    public static final String ee_90 = "[ee_90]";
    public static final String ee_91 = "[ee_91]";
    public static final String ee_92 = "[ee_92]";
    public static final String ee_93 = "[ee_93]";
    public static final String ee_94 = "[ee_94]";
    public static final String ee_95 = "[ee_95]";
    public static final String ee_96 = "[ee_96]";
    public static final String ee_97 = "[ee_97]";
    public static final String ee_98 = "[ee_98]";
    public static final String ee_99 = "[ee_99]";

    public static final String ee_100 = "[ee__100]";
    public static final String ee_101 = "[ee__101]";
    public static final String ee_102 = "[ee__102]";
    public static final String ee_103 = "[ee__103]";
    public static final String ee_104 = "[ee__104]";
    public static final String ee_105 = "[ee__105]";
    public static final String ee_106 = "[ee__106]";
    public static final String ee_107 = "[ee__107]";
    public static final String ee_108 = "[ee__108]";
    public static final String ee_109 = "[ee__109]";
    public static final String ee_110 = "[ee__110]";
    public static final String ee_111 = "[ee__111]";
    public static final String ee_112 = "[ee__112]";
    public static final String ee_113 = "[ee__113]";
    public static final String ee_114 = "[ee__114]";
    public static final String ee_115 = "[ee__115]";
    public static final String ee_160 = "[ee__160]";
    public static final String ee_161 = "[ee__161]";
    public static final String ee_162 = "[ee__162]";
    public static final String ee_163 = "[ee__163]";
    public static final String ee_164 = "[ee__164]";
    public static final String ee_165 = "[ee__165]";
    public static final String ee_166 = "[ee__166]";
    public static final String ee_167 = "[ee__167]";

    private static final Factory spannableFactory = Spannable.Factory.getInstance();

    private static final Map<Pattern, Object> emoticons = new HashMap<>();

    static {
        EaseEmojicon[] emojicons = EaseDefaultEmojiconDatas.getData();
        for (EaseEmojicon emojicon : emojicons) {
            addPattern(emojicon.getEmojiText(), emojicon.getIcon());
        }
        EaseEmojiconInfoProvider emojiconInfoProvider = EaseUI.getInstance().getEmojiconInfoProvider();
        if (emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null) {
            for (Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()) {
                addPattern(entry.getKey(), entry.getValue());
            }
        }

    }

    /**
     * add text and icon to the map
     *
     * @param emojiText-- text of emoji
     * @param icon        -- resource id or local path
     */
    public static void addPattern(String emojiText, Object icon) {
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Object value = entry.getValue();
                    if (value instanceof String && !((String) value).startsWith("http")) {
                        File file = new File((String) value);
                        if (!file.exists() || file.isDirectory()) {
                            return false;
                        }
                        spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new ImageSpan(context, (Integer) value),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static int getSmilesSize() {
        return emoticons.size();
    }


}
