package com.example.questionnaire.repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.vo.QnQuVo;


@Repository
public interface QuestionnaireDao extends JpaRepository<Questionnaire, Integer> {

	public List<Questionnaire> findByIdIn(List<Integer> idList);

	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String title,
			LocalDate startDate, LocalDate endDate);

	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(
			String title, LocalDate startDate, LocalDate endDate);
	
	// =========================================================================
	// SQL語法 // 新增 insert
	// PK 如果存在，無法 insert
	@Modifying
	@Transactional
	@Query(value = "insert into questionnaire (title,description,is_published,start_date,end_date)"
			// values括號裡面指的是@Param("")裡面的名稱
			+ "values(:title,:description,:published,:startDate,:endDate)", nativeQuery = true)
	public int insert(@Param("title") String title, //
			@Param("description") String description, //
			@Param("published") boolean published, //
			@Param("startDate") LocalDate startDate, //
			@Param("endDate") LocalDate endDate);

	@Modifying
	@Transactional
	@Query(value = "insert into questionnaire (title,description,is_published,start_date,end_date)"
			+ " values(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	public int insertData(String title, //
			String description, //
			boolean published, //
			LocalDate startDate, //
			LocalDate endDate);

	// ==========================================================================================
	// update
	@Modifying
	@Transactional
	@Query(value = "update questionnaire set title = :title, description= :description"
			+ " where id = :id", nativeQuery = true)
	public int update(@Param("id") int id, //
			@Param("title") String title, //
			@Param("description") String description);

	/*
	 * 不寫 nativeQuery 等同於 nativeQuery = false \n 語法中表的名稱要變成 entity 的 class 名稱:
	 * 欄位名稱要變成屬性名稱 clearAutomatically = true: 清除持久化上下文，即清除暫存資料
	 */
	@Modifying
	@Transactional
	@Query(value = "update Questionnaire set title = :title, description= :description, startDate = :startDate"
			+ " where id = :id")
	public int updateData(//
			@Param("id") int id, //
			@Param("title") String title, //
			@Param("description") String description, //
			@Param("startDate") LocalDate startDate);
	
	
	//更改問卷狀態
	@Modifying
	@Transactional
	@Query(value = "update Questionnaire set published = true where startDate = :today")
	public int updateQnStatus(@Param("today")LocalDate today);

			
	// ===============================================================
	// select
	@Query(value = "select *from questionnaire " //
			+ " where start_date > :startDate", nativeQuery = true)
	public List<Questionnaire> findByStartDate(@Param("startDate") LocalDate startDate);

	@Query(value = "select new Questionnaire(id, title, description, published, startDate,  endDate)"
			+ " from Questionnaire where startDate > :startDate")
	public List<Questionnaire> findByStartDate1(@Param("startDate") LocalDate startDate);

	// nativeQuery = false, select 的欄位要使用建構方法，且 Entity 中要建立建構方法
	@Query(value = "select new Questionnaire(id, title, published)"
			+ " from Questionnaire where startDate > :startDate")
	public List<Questionnaire> findByStartDate2(@Param("startDate") LocalDate startDate);

	// 使用別名 as
	@Query(value = "select qu from Questionnaire as qu" + "  where startDate > :startDate or published = :published")
	public List<Questionnaire> findByStartDate3(@Param("startDate") LocalDate startDate,
			@Param("published") boolean published);

	// orde rby
	@Query(value = "select qu from Questionnaire as qu"
			+ "  where startDate > :startDate or published = :published order by id desc ")
	public List<Questionnaire> findByStartDate4(//
			@Param("startDate") LocalDate startDate, //
			@Param("published") boolean published); //

	// order by + limit
	// 1.limit 語法只能使用在 nativeQuery = true
	// 2.limit 要放在語法的最後
	@Query(value = "select *from questionnaire as qu"
			+ "  where start_date > :startDate or published = :published order by id desc limit :num"//
			, nativeQuery = true)
	public List<Questionnaire> findByStartDate5(//
			@Param("startDate") LocalDate startDate, //
			@Param("published") boolean published, //
			@Param("num") int limitnum);

	@Query(value = "select * from questionnaire" + " limit :startIndex, :limitNum", nativeQuery = true)
	public List<Questionnaire> findWithLimitAndStartIndex(@Param("startIndex") int startIndex, //
			@Param("limitNum") int limitNum);

	// like
	@Query(value = "select * from questionnaire" + " where title like %:title%", nativeQuery = true)
	public List<Questionnaire> searchTitleLike(@Param("title") String title);

	// regexp
	@Query(value = "select * from questionnaire" + " where title regexp :title", nativeQuery = true)
	public List<Questionnaire> searchTitleLike2(@Param("title") String title);

	// regexp or //方法1
	@Query(value = "select * from questionnaire" //
			+ "where description regexp :keywoed1|:keywoed2", nativeQuery = true)
	public List<Questionnaire> searchDescriptionContaining(@Param("keywoed1") String keywoed1,
			@Param("keywoed2") String keywoed2);
	//方法2
	@Query(value = "select * from questionnaire" //
			+ " where description regexp concat(:keywoed1,'|', :keywoed2)", nativeQuery = true)
	public List<Questionnaire> searchDescriptionContaining2(//
			@Param("keywoed1") String keywoed1,
			@Param("keywoed2") String keywoed2);
	
	//=============================================================
	//join
	@Query("select new com.example.questionnaire.vo.QnQuVo ("
			+ " qn.id, qn.title, qn.description, qn.published, qn.startDate, qn.endDate, "
			+ " qu.quId, qu.qTitle, qu.optionType, qu.necessary, qu.option) "
			+ " from Questionnaire as qn join Question as qu on qn.id = qu.qnId")
	public List<QnQuVo> selectJoinQnQu();
	
	@Query("select new com.example.questionnaire.vo.QnQuVo("
			+ " qn.id, qn.title, qn.description, qn.published, qn.startDate,  qn.endDate, "
			+ " q.quId, q.qTitle, q.optionType, q.necessary, q.option)"
			+ " from Questionnaire as qn join Question as q on qn.id = q.qnId"
			+ " where qn.title like %:title and qn.startDate >= :startDate and qn.endDate <= :endDate")
	public List<QnQuVo> selectFuzzy(//
			@Param("title")String title, //
			@Param("startDate")LocalDate startDate, //
			@Param("endDate")LocalDate endDate);
}
