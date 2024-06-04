import { useEffect, useState } from "react";
import './SentimentReport.css';
import {getCookie} from '../utils/cookieManage';

function SentimentReport(props) {
  let [date, setDate] = useState(new Date());
  let [beforeDate, setBeforeDate] = useState();
  let [emotions, setEmotions] = useState([]);
  let [filteredEmotion, setFilteredEmotion] = useState({});

  const getEmotions = async (year, month) => {
    let res = await fetch(process.env.REACT_APP_API_URL + `/api/feeling/month?year=${year}&month=${month}`, {
      headers: {
        authorization: 'Bearer ' + getCookie('accessToken')
      }
    });

    if (!res.ok && res.status !== 404) {
      let message = await res.text();
      throw new Error(message);
    } else if (res.status === 404) {
      return [];
    }

    return res.json();
  }

  const filterEmotions = (dates) => {
    let result = {}
    dates.forEach(day => {
      let thisEmotion = emotions.filter(item => {
        let formmatedDate = `${date.getFullYear()}-${('0' + (date.getMonth() + 1)).slice(-2)}-${('0' + day).slice(-2)}`
        return formmatedDate === item.date;
      });
      
      result[`${day}`] = thisEmotion.map((item) => item.emotion)
      result[`${day}`] = result[`${day}`].slice(0, 2);
    });
    setFilteredEmotion(result);
  }


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


  useEffect(() => {
    if (beforeDate !== date) {
      getEmotions(date.getFullYear(), date.getMonth() + 1)
        .then((data) => {
          setEmotions(data);
        })
        .catch((error) => {
          alert(error);
        });
        setBeforeDate(date);
    } else {
      filterEmotions(thisDates);
    }
    // eslint-disable-next-line
  }, [date, emotions]);
  
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
                  <div className="icon-container">
                    {
                      filteredEmotion[item]?.map((emotion, idx) => {
                        if (i >= firstIndex && i <= lastIndex) {
                          switch (emotion) {
                            case 'positive':
                              return (<img className="icon" src={require('../assets/icon_heart.png')} alt="emotion" key={idx} />);
                            case 'negative':
                              return (<img className="icon" src={require('../assets/icon_water.png')} alt="emotion" key={idx} />);
                            case 'neutral':
                              return (<img className="icon" src={require('../assets/icon_cloud.png')} alt="emotion" key={idx} />);
                            default:
                              return null;
                          }
                        }
                        return null;
                      })
                    }
                  </div>
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