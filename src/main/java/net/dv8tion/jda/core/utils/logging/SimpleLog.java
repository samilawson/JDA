/*
 *     Copyright 2015-2017 Austin Keener & Michael Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.core.utils.logging;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.PrintStream;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class SimpleLog implements Logger
{
    public static final byte TRACE = 0x1;
    public static final byte DEBUG = 0x2;
    public static final byte INFO  = 0x4;
    public static final byte WARN  = 0x8;
    public static final byte ERROR = 0x10;
    public static final int  ALL = TRACE | DEBUG | INFO | WARN | ERROR;

    private static int defaultLevel = INFO | WARN | ERROR;

    private final String name;
    private int staticLevel = defaultLevel;
    private Map<Marker, Integer> mappedLevels = new HashMap<>();


    public SimpleLog(String name)
    {
        this.name = name;
    }

    public static String timestamp()
    {
        final OffsetDateTime now = OffsetDateTime.now();
        final long hour = now.getHour();
        final long minute = now.getMinute();
        final long second = now.getSecond();
        return String.format("[%s:%s:%s]",
                (hour < 10 ? "0" : "") + hour,
                (minute < 10 ? "0" : "") + minute,
                (second < 10 ? "0" : "") + second);
    }

    public static void setDefaultLevel(int level)
    {
        defaultLevel = level;
    }

    public static int getDefaultLevel()
    {
        return defaultLevel;
    }

    public void setStaticLevel(int newLevel)
    {
        this.staticLevel = newLevel;
    }

    public void enableLevel(int level)
    {
        this.staticLevel = (short) (staticLevel & ~level);
    }

    public void disableLevel(int level)
    {
        this.staticLevel |= level;
    }

    public int setMarkerLevel(Marker marker, int level)
    {
        return mappedLevels.put(marker, level);
    }

    public int enableMarkerLevel(Marker marker, int level)
    {
        return mappedLevels.compute(marker, (key, value) -> (value != null ? value : 0) | level);
    }

    public int disableMarkerLevel(Marker marker, int level)
    {
        return mappedLevels.compute(marker, (key, value) -> (value != null ? value : 0) & ~level);
    }

    private void log(byte level, Marker marker, String format, Object... arguments)
    {
        try
        {
            if ((mappedLevels.getOrDefault(marker, staticLevel) & level) < 1)
                return;
            String time = timestamp() + " ";
            String levelS = "";
            if (level == TRACE)
                levelS = "[TRACE] ";
            else if (level == DEBUG)
                levelS = "[DEBUG] ";
            else if (level == INFO)
                levelS = "[INFO] ";
            else if (level == WARN)
                levelS = "[WARN] ";
            else if (level == ERROR)
                levelS = "[ERROR] ";

            List<String> markers = new LinkedList<>();
            if (marker != null)
            {
                for (Iterator<Marker> it = marker.iterator(); it.hasNext(); )
                {
                    Marker current = it.next();
                    if (current == null) break;
                    String name = current.getName();
                    if (name != null && !name.isEmpty())
                        markers.add(String.format("[%s]", name));
                }
            }

            String tags = markers.isEmpty() ? null : markers.stream().collect(Collectors.joining(" ", "", " "));

            PrintStream out = level == ERROR ? System.err : System.out;
            out.printf("%s%s%s[%s] %s%n", time, levelS, tags != null ? tags : "", name,
                    format(format, arguments));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isTraceEnabled()
    {
        return (staticLevel & TRACE) > 0;
    }

    @Override
    public void trace(String msg)
    {
        log(TRACE, null, msg);
    }

    @Override
    public void trace(String format, Object arg)
    {
        log(TRACE, null, format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2)
    {
        log(TRACE, null, format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments)
    {
        log(TRACE, null, format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t)
    {
        if (msg != null)
            trace(msg);
        else trace("");
        t.printStackTrace();
    }

    @Override
    public boolean isTraceEnabled(Marker marker)
    {
        return (mappedLevels.getOrDefault(marker, staticLevel) & TRACE) > 0;
    }

    @Override
    public void trace(Marker marker, String msg)
    {
        log(TRACE, marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg)
    {
        log(TRACE, marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2)
    {
        log(TRACE, marker, format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray)
    {
        log(TRACE, marker, format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t)
    {
        if (msg != null)
            trace(marker, msg);
        else trace(marker, "");
        t.printStackTrace();
    }

    @Override
    public boolean isDebugEnabled()
    {
        return (staticLevel & DEBUG) > 0;
    }

    @Override
    public void debug(String msg)
    {
        log(DEBUG, null, msg);
    }

    @Override
    public void debug(String format, Object arg)
    {
        log(DEBUG, null, format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2)
    {
        log(DEBUG, null, format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments)
    {
        log(DEBUG, null, format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t)
    {
        if (msg != null)
            debug(msg);
        else debug("");
        t.printStackTrace();
    }

    @Override
    public boolean isDebugEnabled(Marker marker)
    {
        return (mappedLevels.getOrDefault(marker, staticLevel) & DEBUG) > 0;
    }

    @Override
    public void debug(Marker marker, String msg)
    {
        log(DEBUG, marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg)
    {
        log(DEBUG, marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2)
    {
        log(DEBUG, marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments)
    {
        log(DEBUG, marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t)
    {
        if (msg != null)
            debug(marker, msg);
        else debug(marker, "");
        t.printStackTrace();
    }

    @Override
    public boolean isInfoEnabled()
    {
        return (staticLevel & INFO) > 0;
    }

    @Override
    public void info(String msg)
    {
        log(INFO, null, msg);
    }

    @Override
    public void info(String format, Object arg)
    {
        log(INFO, null, format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2)
    {
        log(INFO, null, format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments)
    {
        log(INFO, null, format, arguments);
    }

    @Override
    public void info(String msg, Throwable t)
    {
        if (msg != null)
            info(msg);
        else info("");
        t.printStackTrace();
    }

    @Override
    public boolean isInfoEnabled(Marker marker)
    {
        return (mappedLevels.getOrDefault(marker, staticLevel) & INFO) > 0;
    }

    @Override
    public void info(Marker marker, String msg)
    {
        log(INFO, marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg)
    {
        log(INFO, marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2)
    {
        log(INFO, marker, format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments)
    {
        log(INFO, marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t)
    {
        if (msg != null)
            info(marker, msg);
        else info(marker, "");
        t.printStackTrace();
    }

    @Override
    public boolean isWarnEnabled()
    {
        return (staticLevel & WARN) > 0;
    }

    @Override
    public void warn(String msg)
    {
        log(WARN, null, msg);
    }

    @Override
    public void warn(String format, Object arg)
    {
        log(WARN, null, format, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2)
    {
        log(WARN, null, format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object... arguments)
    {
        log(WARN, null, format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t)
    {
        if (msg != null)
            warn(msg);
        else warn("");
        t.printStackTrace();
    }

    @Override
    public boolean isWarnEnabled(Marker marker)
    {
        return (mappedLevels.getOrDefault(marker, staticLevel) & WARN) > 0;
    }

    @Override
    public void warn(Marker marker, String msg)
    {
        log(WARN, marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg)
    {
        log(WARN, marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2)
    {
        log(WARN, marker, format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments)
    {
        log(WARN, marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t)
    {
        if (msg != null)
            warn(marker, msg);
        else warn(marker, "");
        t.printStackTrace();
    }

    @Override
    public boolean isErrorEnabled()
    {
        return (staticLevel & ERROR) > 0;
    }

    @Override
    public void error(String msg)
    {
        log(ERROR, null, msg);
    }

    @Override
    public void error(String format, Object arg)
    {
        log(ERROR, null, format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2)
    {
        log(ERROR, null, format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments)
    {
        log(ERROR, null, format, arguments);
    }

    @Override
    public void error(String msg, Throwable t)
    {
        if (msg != null)
            error(msg);
        else error("");
        t.printStackTrace();
    }

    @Override
    public boolean isErrorEnabled(Marker marker)
    {
        return (mappedLevels.getOrDefault(marker, staticLevel) & ERROR) > 0;
    }

    @Override
    public void error(Marker marker, String msg)
    {
        log(ERROR, marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg)
    {
        log(ERROR, marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2)
    {
        log(ERROR, marker, format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments)
    {
        log(ERROR, marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t)
    {
        if (msg != null)
            error(marker, msg);
        else error(marker, "");
        t.printStackTrace();
    }

    private static final String BRACKETS = "\\{[^}]*}";

    private static String format(String format, Object... args)
    {
        if (args != null)
        {
            for (Object a : args)
            {
                if (a instanceof Supplier)
                    format = format.replaceFirst(BRACKETS, sanitized(((Supplier) a).get()));
                else
                    format = format.replaceFirst(BRACKETS, sanitized(a));
            }
        }
        return format;
    }

    private static String sanitized(Object input)
    {
        return Matcher.quoteReplacement(String.valueOf(input));
    }
}
