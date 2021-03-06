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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.cache.CrestCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.PledgeCrest;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPledgeCrest extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private int _clanId;
	private int _crestId;
	
	@Override
	protected void readImpl()
	{
		_crestId = readD();
		_clanId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		if (_crestId == 0)
		{
			return;
		}
		
		byte[] data = CrestCache.getInstance().getPledgeCrest(_crestId);
		
		if (data == null)
		{
			return;
		}
		
		PledgeCrest pc = new PledgeCrest(_crestId, data);
		sendPacket(pc);
	}
}
