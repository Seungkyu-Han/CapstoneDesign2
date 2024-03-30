import './DiaryList.css';

function DiaryList(props) {
  return (
    <div class="diary-list-container">
      {/* 여기에 다이어리 리스트 페이지 만들면 됨 */}
      <div class="diary-list-header">
        <div class="time-select">
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
        <button class="create-diary-btn">일기 작성</button>
      </div>
      <div class="diary-list-main">
        <div class="diary-wrapper">
          <div class="diary-color"></div>
          <div class="diary-date">2024년 3월 25일</div>
          <div class="diary-content">
            <div class="diary-title">바귀벌레</div>
            <div class="diary-text">바퀴벌레한마리가 있다. 그때바퀴벌레가 날개를 푸드드득거리며 나에게로 날아온다. 나는 공포에 질렸다. 파리채를 휘두르며 저항하였지만 결국 나에게 도달했다....</div>
          </div>
        </div>
        <div class="diary-wrapper">
        <div class="diary-color"></div>
          <div class="diary-date">2024년 3월 25일</div>
          <div class="diary-content">
            <div class="diary-title">바귀벌레</div>
            <div class="diary-text">바퀴벌레한마리가 있다. 그때바퀴벌레가 날개를 푸드드득거리며 나에게로 날아온다. 나는 공포에 질렸다. 파리채를 휘두르며 저항하였지만 결국 나에게 도달했다....</div>
          </div>
        </div>
        <div class="diary-wrapper">
          <div class="diary-color"></div>
          <div class="diary-date">2024년 3월 25일</div>
          <div class="diary-content">
            <div class="diary-title">바귀벌레</div>
            <div class="diary-text">바퀴벌레한마리가 있다. 그때바퀴벌레가 날개를 푸드드득거리며 나에게로 날아온다. 나는 공포에 질렸다. 파리채를 휘두르며 저항하였지만 결국 나에게 도달했다....</div>
          </div>
        </div>
        <div class="diary-wrapper">
          <div class="diary-color"></div>
          <div class="diary-date">2024년 3월 25일</div>
          <div class="diary-content">
            <div class="diary-title">바귀벌레</div>
            <div class="diary-text">바퀴벌레한마리가 있다. 그때바퀴벌레가 날개를 푸드드득거리며 나에게로 날아온다. 나는 공포에 질렸다. 파리채를 휘두르며 저항하였지만 결국 나에게 도달했다....</div>
          </div>
        </div>
        <div class="diary-wrapper">
          <div class="diary-color"></div>
          <div class="diary-date">2024년 3월 25일</div>
          <div class="diary-content">
            <div class="diary-title">바귀벌레</div>
            <div class="diary-text">바퀴벌레한마리가 있다. 그때바퀴벌레가 날개를 푸드드득거리며 나에게=파리채를 휘두르며 저항하였지만 결국 나에게 도달했다....</div>
          </div>
        </div>
        <div class="diary-wrapper">
          <div class="diary-color"></div>
          <div class="diary-date">2024년 3월 25일</div>
          <div class="diary-content">
            <div class="diary-title">바귀벌레</div>
            <div class="diary-text">바퀴벌레한마리가 있다. 그때바퀴벌레가 날개를 푸드드득거리며 나에게로 날아온다. 나는 공포에 질렸다. 파리채를 휘두르며 저항하였지만 결국 나에게 도달했다....</div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default DiaryList;