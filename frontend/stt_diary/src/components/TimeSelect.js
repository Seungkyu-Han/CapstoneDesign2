import React, { useState, useEffect } from 'react';

export function TimeSelect({handleTimeSelectChange}) {
    const [timeData, setTimeData] = useState([]);
    useEffect(() => {
        fetch(`${process.env.REACT_APP_API_URL}/api/diary/all?userId=1`,
            {
              headers: {'Content-Type': 'application/json'},
            })
            .then((response) => response.json())
            .then((data) => {
                setTimeData(data);
            })
            .catch((error) => console.log(error));
    }, []);

    return (
        <div className="time-select">
          <select id="yearSelect" onChange={handleTimeSelectChange}>
              {timeData.length > 0 ? (
                (() => {
                    const end = parseInt(timeData[0].date.split('-')[0]);
                    const start = parseInt(timeData[timeData.length - 1].date.split('-')[0]);
                    const yearOptions = [];
                    for (let i = end; i >= start; i--) {
                      yearOptions.push(<option key={i} value={i}>{i}년</option>);
                    }
                    return yearOptions;
                })()            
              ) : (
                  <option>{new Date().getFullYear()}년</option>
              )}
          </select>

          <select id="monthSelect" onChange={handleTimeSelectChange}>
              <option key ="1" value="1">1월</option>
              <option key ="2" value="2">2월</option>
              <option key ="3" value="3">3월</option>
              <option key ="4" value="4">4월</option>
              <option key ="5" value="5">5월</option>
              <option key ="6" value="6">6월</option>
              <option key ="7" value="7">7월</option>
              <option key ="8" value="8">8월</option>
              <option key ="9" value="9">9월</option>
              <option key ="10" value="10">10월</option>
              <option key ="11" value="11">11월</option>
              <option key ="12" value="12">12월</option>
          </select>
        </div>
    )
}
