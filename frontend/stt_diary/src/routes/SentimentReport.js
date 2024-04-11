import { useState } from "react";
import './SentimentReport.css';

function SentimentReport(props) {
  let [date, setDate] = useState(new Date());

  const prevLast = new Date(date.getFullYear(), date.getMonth(), 0);
  const thisLast = new Date(date.getFullYear(), date.getMonth() + 1, 0);

  const prevLastDay = prevLast.getDay();
  const prevLastDate = prevLast.getDate();

  const thisLastDay = thisLast.getDay();
  const thisLastDate = thisLast.getDate();

  const prevDates = [];
  const thisDates = [...Array(thisLastDate + 1).keys()].slice(1);    
  const nextDates = [];

  if (prevLastDay !== 6) {
    for (let i = 0; i < prevLastDay + 1; i++) {
      prevDates.unshift(prevLastDate - i);
    }
  }

  for (let i = 1; i < 7 - thisLastDay; i++) {
    nextDates.push(i);
  }

  const dates = prevDates.concat(thisDates, nextDates);

  const firstIndex = dates.indexOf(1);
  const lastIndex = dates.lastIndexOf(thisLastDate);
  
  return (
    <div className="report-wrapper">
      <div className="header">
        <h3 className="date">
          <img src={require('../assets/calendar_month.png')} alt="calendar_month"/>
          {date.getFullYear()}년 {date.getMonth() + 1}월
          <span className="date-btn-container">
            <button className="date-btn" onClick={() => {
              let newDate = new Date(date);
              newDate.setMonth(date.getMonth() - 1)
              setDate(newDate);
            }}>
              <img src={require('../assets/arrow_drop_down.png')} alt="arrow_drop_down"/>
            </button>
            <button className="date-btn" onClick={() => {
              let newDate = new Date(date);
              newDate.setMonth(date.getMonth() + 1)
              setDate(newDate);
            }}>
              <img src={require('../assets/arrow_drop_up.png')} alt="arrow_drop_up"/>
            </button>
          </span>
        </h3>
      </div>
      <div className="calendar">
        <div className="calendar-header">
          <div>일</div>
          <div>월</div>
          <div>화</div>
          <div>수</div>
          <div>목</div>
          <div>금</div>
          <div>토</div>
        </div>
        <div className="calendar-body">
          {
            dates.map((item, i) => {
              return(
                <div className="date-item" key={i}>
                  <span className={i >= firstIndex && i <= lastIndex ? "this" : "other"}>{item}</span>
                </div>
              )
            })
          }
        </div>
      </div>
    </div>
  )
}

export default SentimentReport;