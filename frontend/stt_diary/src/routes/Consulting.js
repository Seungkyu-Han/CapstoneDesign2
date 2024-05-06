import { useEffect, useState } from 'react';
import './Consulting.css';

function Consulting() {
  let [message, setMessage] = useState([]);

  useEffect(() => {
    const target = document.getElementsByClassName('chat-message')[0];
    const targetWrap = document.getElementsByClassName('chat-message-container')[0];
    targetWrap.scrollTo({left: 0, top: target.offsetHeight, behavior: 'smooth'});
  }, [message]);

  return (
    <div className='chat-box-wrapper'>
      <div className='chat-box-wrapper-inner'>
        <div className='chat-box'>
          <button id='mobile-exit-btn'>
            <img src={require('../assets/arrow_back.png')} alt='arrow_back'/>
          </button>
          <div className='chat-message-container-wrapper'>
            <div className='chat-message-container'>
              <div className='chat-message'>
                <div className='chat bot'>
                  <img className='bot-profile' src={require('../assets/chat-profile.png')} alt='bot-profile' />
                  <div className='message'>
                    안녕하세요! 심리상담 챗봇입니다. <br/>
                    어떤 문제로 도와드릴까요?
                  </div>
                </div>
                {
                  message.map((item, i) => {
                    return (
                      <div className={'chat ' + item.role} key={i}>
                        {
                          item.role === 'bot' ? 
                            <img className='bot-profile' src={require('../assets/chat-profile.png')} alt='bot-profile'/> : 
                            null
                        }
                        <div className='message'>
                          {item.content}
                        </div>
                      </div>
                    )
                  })
                }
              </div>
            </div>
          </div>
          <div className='chat-input-wrapper'>
            <div className='chat-input-wrap-inner'>
              <input type='text' id='chat-input' placeholder='질문을 입력하세요.' onKeyUp={(e) => {
                if (e.key === "Enter") {
                  document.getElementById("submit-btn").click();
                }
              }}/>
              <button id='submit-btn' onClick={() => {
                let input = document.getElementById("chat-input");
                const content = input.value;
                if (content.length > 0) {
                  let messageCopy = [...message];
                  messageCopy.push({ role: "user", content: content });
                  input.value = "";
                  messageCopy.push({ role: "bot", content: "잘 모르겠어요." });  // 테스트 용 더미 메시지
                  setMessage(messageCopy);
                }
              }}>
                <img src={require('../assets/arrow_circle_up.png')} alt='arrow_circle_up' />
              </button>
            </div>
          </div>
        </div>
        <div className='exit-btn-wrapper'>
          <button id='exit-btn'>
            상담 종료
          </button>
        </div>
      </div>
    </div>
  )
}

export default Consulting;