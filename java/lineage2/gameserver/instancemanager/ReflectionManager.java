/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.instancemanager;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.gameserver.data.xml.holder.DoorHolder;
import lineage2.gameserver.data.xml.holder.ZoneHolder;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.utils.Location;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ReflectionManager
{
	public static final Reflection DEFAULT = Reflection.createReflection(0);
	public static final Reflection PARNASSUS = Reflection.createReflection(-1);
	public static final Reflection GIRAN_HARBOR = Reflection.createReflection(-2);
	public static final Reflection JAIL = Reflection.createReflection(-3);
	public static final Reflection CTF_EVENT = Reflection.createReflection(-4);
	public static final Reflection TVT_EVENT = Reflection.createReflection(-5);
	public static final Reflection LAST_HERO = Reflection.createReflection(-6);
	private static final ReflectionManager _instance = new ReflectionManager();
	private final TIntObjectHashMap<Reflection> _reflections = new TIntObjectHashMap<>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	
	/**
	 * Method getInstance.
	 * @return ReflectionManager
	 */
	public static ReflectionManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for ReflectionManager.
	 */
	private ReflectionManager()
	{
		add(DEFAULT);
		add(PARNASSUS);
		add(GIRAN_HARBOR);
		add(JAIL);
		add(ReflectionManager.TVT_EVENT);
		add(ReflectionManager.CTF_EVENT);
		add(ReflectionManager.LAST_HERO);
		DEFAULT.init(DoorHolder.getInstance().getDoors(), ZoneHolder.getInstance().getZones());
		HashMap<String, ZoneTemplate> _dummy = new HashMap<>();
		TVT_EVENT.init(DoorHolder.getInstance().getDoors(), _dummy);
		CTF_EVENT.init(DoorHolder.getInstance().getDoors(), _dummy);
		LAST_HERO.init(DoorHolder.getInstance().getDoors(), _dummy);
		JAIL.setCoreLoc(new Location(-114648, -249384, -2984));
	}
	
	/**
	 * Method get.
	 * @param id int
	 * @return Reflection
	 */
	public Reflection get(int id)
	{
		readLock.lock();
		
		try
		{
			return _reflections.get(id);
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method add.
	 * @param ref Reflection
	 * @return Reflection
	 */
	public Reflection add(Reflection ref)
	{
		writeLock.lock();
		
		try
		{
			return _reflections.put(ref.getId(), ref);
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method remove.
	 * @param ref Reflection
	 * @return Reflection
	 */
	public Reflection remove(Reflection ref)
	{
		writeLock.lock();
		
		try
		{
			return _reflections.remove(ref.getId());
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method getAll.
	 * @return Reflection[]
	 */
	public Reflection[] getAll()
	{
		readLock.lock();
		
		try
		{
			return _reflections.values(new Reflection[_reflections.size()]);
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method getCountByIzId.
	 * @param izId int
	 * @return int
	 */
	public int getCountByIzId(int izId)
	{
		readLock.lock();
		
		try
		{
			int i = 0;
			
			for (Reflection r : getAll())
			{
				if (r.getInstancedZoneId() == izId)
				{
					i++;
				}
			}
			
			return i;
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	public int size()
	{
		return _reflections.size();
	}
}
