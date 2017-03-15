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

import net.dv8tion.jda.core.JDA;
import org.slf4j.Marker;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ShardMarker implements Marker
{

    private final String shard;
    private final List<Marker> children = new LinkedList<>();

    public ShardMarker(JDA.ShardInfo info)
    {
        this.shard = info != null ? info.getShardId() + "/" + info.getShardTotal() : null;
    }

    @Override
    public String getName()
    {
        return shard;
    }

    @Override
    public void add(Marker reference)
    {
        if (!children.contains(reference))
            children.add(reference);
    }

    @Override
    public boolean remove(Marker reference)
    {
        return children.remove(reference);
    }

    @Override
    public boolean hasChildren()
    {
        return !children.isEmpty();
    }

    @Override
    public boolean hasReferences()
    {
        return !children.isEmpty();
    }

    @Override
    public Iterator<Marker> iterator()
    {
        List<Marker> view = new LinkedList<>(children);
        view.add(0, this);
        return view.iterator();
    }

    @Override
    public boolean contains(Marker other)
    {
        return false;
    }

    @Override
    public boolean contains(String name)
    {
        return false;
    }


}
