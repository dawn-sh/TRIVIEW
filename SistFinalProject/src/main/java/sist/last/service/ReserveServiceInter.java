package sist.last.service;

import java.util.List;

import sist.last.dto.ReserveDto;
import sist.last.dto.RoomDto;

public interface ReserveServiceInter {

	public void reservingInsert(ReserveDto dto);
	public ReserveDto getOneDataByIdOfReserve(String info_id,int idx);
	public int getMaxIdxOfReserve();
	public ReserveDto getOneInfoData(String info_id);
	public List<ReserveDto> getReserveCountOfRoomNumber(int room_num);
	public List<ReserveDto> getReservationDataById(String info_id);

	public void updateReserveStatus(ReserveDto dto);
	public String getPhoto(String room_num);
}
