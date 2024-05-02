import { useState } from 'react';
import './Consulting.css';

function Consulting() {
  let [message, setMessage] = useState("");

  return (
    <div className="chat-container-wrapper">
      <div className='chat-container'>
        <div className="chat-container-inner">
          <div className="chat bot">
            <img src={require("../assets/chat-profile.png")} alt='chat-profile' className='chat-profile'/>
            <div className="message">
              안녕하세요! 심리상담 챗봇입니다. <br/>어떤 문제로 도와드릴까요?
            </div>
          </div>
          <div className="chat user">
            <div className="message">
              요즘 너무 스트레스 받아요. 일상 생활에 집중이 안돼요.
            </div>
          </div>
        </div>
        <div className='chat-input-wrapper'>
          <div className='chat-input-inner'>
            <input type='text' id='chat-input' placeholder='질문을 입력하세요.' onChange={(e) => {
              setMessage(e.target.value);
            }} onKeyDown={(e) => {
              if (e.key === 'Enter') {
                console.log(message);
                e.target.value = "";
              }
            }}/>
            <button id='submit-btn'>
              <img src={require('../assets/arrow_circle_up.png')} alt='arrow_circle_up' onClick={() => {
                console.log(message);
                document.getElementById("chat-input").value = "";
              }}/>
            </button>
          </div>
        </div>
      </div>
      <button id='exit-btn'>
        상담 종료
      </button>
    </div>
  )
}

export default Consulting;