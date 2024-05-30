import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './DiaryList.css';
import { TimeSelect } from '../components/TimeSelect';
import { Link } from 'react-router-dom';
import { getCookie } from '../utils/cookieManage';

function DiaryList() {
  const [data, setData] = useState([]);
  const [year, setYear] = useState(new Date().getFullYear());
  const [month, setMonth] = useState(new Date().getMonth() + 1);
  const navigate = useNavigate();

  useEffect(() => {
    if(getCookie("accessToken")){
      fetch(`${process.env.REACT_APP_API_URL}/api/diary/month?&year=${year}&month=${month}`,
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getCookie("accessToken"),
          },
        })
      .then((response) => {
          if (response.status === 200) {
            return response.json();
          } else {
            throw new Error();
          }
      })
      .then((data) => {
        setData(data);
      })
      .catch((error) => {
         alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
      });
    }
    else{
      navigate('/login');
    }
  }, [year,month]);
  
  const handleTimeSelectChange = () => {
    setYear(parseInt(document.getElementById('yearSelect').value));
    setMonth(parseInt(document.getElementById('monthSelect').value));
  };
  const handleCreateDiaryClick = () => {
    navigate('/create-diary');
  };

  return (
    <div className="diary-list-container">
      <div className="diary-list-header">
        <TimeSelect handleTimeSelectChange={handleTimeSelectChange} year={year} month={month}/>
        <button className="create-diary-btn" onClick={handleCreateDiaryClick}>일기 작성</button>
      </div>
      {data && data.length > 0 ? (
        <div className="diary-list-main">{
            data.map((item, i) => {
              let dates = item.date.split('-');
              return (
                <div key={i} className="diary-wrapper">
                  <Link to={`/detail-diary/${item.id}`} className='diary-link'>
                    <div className="diary-color"></div>
                    <div className="diary-date">{dates[0]}년 {dates[1]}월 {dates[2]}일</div>
                    <div className="diary-content">
                      <div className="diary-title">{item.title}</div>
                      <div className="diary-text">{item.content}</div>
                    </div>
                  </Link>
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