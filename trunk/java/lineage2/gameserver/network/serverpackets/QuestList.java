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
package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.QuestState;

/**
 * format: h[dd]b
 */
public class QuestList extends L2GameServerPacket
{
	/**
	 * This text was wrote by XaKa QuestList packet structure: { 1 byte - 0x80 2 byte - Number of Quests for Quest in AvailibleQuests { 4 byte - Quest ID 4 byte - Quest Status } }
	 * <p/>
	 * NOTE: The following special constructs are true for the 4-byte Quest Status: If the most significant bit is 0, this means that no progress-step got skipped. In this case, merely passing the rank of the latest step gets the client to mark it as current and mark all previous steps as complete.
	 * If the most significant bit is 1, it means that some steps may have been skipped. In that case, each bit represents a quest step (max 30) with 0 indicating that it was skipped and 1 indicating that it either got completed or is currently active (the client will automatically assume the
	 * largest step as active and all smaller ones as completed). For example, the following bit sequences will yield the same results: 1000 0000 0000 0000 0000 0011 1111 1111: Indicates some steps may be skipped but each of the first 10 steps did not get skipped and current step is the 10th. 0000
	 * 0000 0000 0000 0000 0000 0000 1010: Indicates that no steps were skipped and current is the 10th. It is speculated that the latter will be processed faster by the client, so it is preferred when no steps have been skipped. However, the sequence "1000 0000 0000 0000 0000 0010 1101 1111"
	 * indicates that the current step is the 10th but the 6th and 9th are not to be shown at all (not completed, either).
	 */
	
	private final List<int[]> questlist;
	private static byte[] unk = new byte[128];
	
	public QuestList(Player player)
	{
		QuestState[] allQuestStates = player.getAllQuestsStates();
		questlist = new ArrayList<>(allQuestStates.length);
		for (QuestState quest : allQuestStates)
		{
			if (quest.getQuest().isVisible(player) && quest.isStarted())
			{
				questlist.add(new int[]
				{
					quest.getQuest().getQuestIntId(),
					quest.getInt(QuestState.VAR_COND)
				});
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x86);
		writeH(questlist.size());
		for (int[] q : questlist)
		{
			writeD(q[0]);
			writeD(q[1]);
		}
		writeB(unk);
	}
}