import React, { useState, useEffect } from 'react';
import './DiaryList.css';
import { TimeSelect } from '../components/TimeSelect';

function DiaryList() {
  const [data, setData] = useState([]);
  const [year, setYear] = useState(new Date().getFullYear());
  const [month, setMonth] = useState(new Date().getMonth() + 1);

  useEffect(() => {
    fetch(`${process.env.REACT_APP_API_URL}/api/diary/month?userId=1&year=${year}&month=${month}`,
        {
          headers: {'Content-Type': 'application/json'},
        })
      .then((response) => response.json())
      .then((data) => {
        setData(data);
      })
      .catch((error) => console.log(error));
  }, [year,month]);

  const handleTimeSelectChange = () => {
    setYear(parseInt(document.getElementById('yearSelect').value));
    setMonth(parseInt(document.getElementById('monthSelect').value));
  };

  return (
    <div className="diary-list-container">
      <div className="diary-list-header">
        <TimeSelect handleTimeSelectChange={handleTimeSelectChange}/>
        <button className="create-diary-btn">일기 작성</button>
      </div>
      {data.length > 0 ? (
        <div className="diary-list-main">{
            data.map((item, i) => {
              let dates = item.date.split('-');
              return (
                <div key={i} className="diary-wrapper">
                  <div className="diary-color"></div>
                  <div className="diary-date">{dates[0]}년 {dates[1]}월 {dates[2]}일</div>
                  <div className="diary-content">
                    <div className="diary-title">{item.title}</div>
                    <div className="diary-text">{item.content}</div>
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