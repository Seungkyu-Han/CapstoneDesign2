import './DiaryList.css';
import { data } from './DiaryListData';

function DiaryList() {
  return (
    <div className="diary-list-container">
      <div className="diary-list-header">
        <div className="time-select">
          <select id="yearSelect" onchange="handleYearSelectChange()">
              <option value="" selected>2024년</option>
              <option value="">2023년</option>
              <option value="">2022년</option>
              <option value="">2021년</option>
          </select>
          <select id="monthSelect" onchange="handleMonthSelectChange()">
              <option value="" selected>3월</option>
              <option value="">2월</option>
              <option value="">1월</option>
          </select>
        </div>
        <button className="create-diary-btn">일기 작성</button>
      </div>
      {data.length > 0 ? (
        <div className="diary-list-main">{
            data.map((item, i) => {
              return (
                <div className="diary-wrapper">
                  <div className="diary-color"></div>
                  <div className="diary-date">{item.date}</div>
                  <div className="diary-content">
                    <div className="diary-title">{item.title}</div>
                    <div className="diary-text">{item.text}</div>
                  </div>
                </div>
              )})
          }
        </div>
      ) : (
        <p className='noDataMessage'>해당 기간에 저장된 일기가 없습니다.</p>
      )}
    </div>
  )
}

export default DiaryList;