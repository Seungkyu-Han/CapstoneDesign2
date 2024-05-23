import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './Consulting.css';
import { getCookie } from '../utils/cookieManage';

function Consulting() {
  const { id } = useParams(); 
  let [message, setMessage] = useState([]);
  let [isLoading, setIsLoading] = useState(false);
  let navigate = useNavigate();

  useEffect(() => {
    const target = document.getElementsByClassName('chat-message')[0];
    const targetWrap = document.getElementsByClassName('chat-message-container')[0];
    targetWrap.scrollTo({left: 0, top: target.offsetHeight, behavior: 'smooth'});
  }, [message]);

  useEffect(() => {
    if (!isLoading) {
      const inputChat = document.getElementById("chat-input");
      inputChat.disabled = false;
      inputChat.focus();
    }
  }, [isLoading]);

  
  useEffect(() => {  //render chat
    let history = [];
    fetch(`${process.env.REACT_APP_API_URL}/api/chatGPT/consult?diaryId=${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getCookie('accessToken'),
      },
    })
    .then(response => {
      if (response.status === 200) {
        return response.json();
      } else {
        alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
        navigate('/');
      }
    })
    .then((res) => {
      res.forEach(item => {
        history.push({ role: "user", content: item.question });
        history.push({ role: "bot", content: item.answer });
      });
      setMessage(history);
    })
    .catch(error => {
      alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
      navigate('/');
    });
  }, []);
  
  useEffect(() => {
    const timer = setTimeout(() => {
      alert('상담이 종료됐습니다.');
      navigate('/');
    }, 1 * 60 * 1000);
    return () => clearTimeout(timer);
  }, [navigate]);

  const sendMessage = (content) => {
    setIsLoading(true);
    let messageCopy = [...message];
    messageCopy.push({ role: "user", content: content });
    setMessage(messageCopy);

    fetch(`${process.env.REACT_APP_API_URL}/api/chatGPT/consult`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getCookie('accessToken'),
      },
      body: JSON.stringify({
        diaryId: id,
        content: content,
      }),
    })
    .then(response => {
      setIsLoading(false);
      if (response.status === 200) {
        return response.json();
      } else {
        setIsLoading(false);
        alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
        navigate('/');
      }
    })
    .then((res) => {
      setMessage(prevMessages => [...prevMessages,{ role: "bot", content: res.answer }]);
    })
    .catch(error => {
      setIsLoading(false);
      alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
      navigate('/');
    });
  }

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
                    안녕하세요! 심리상담 챗봇입니다. 상담은 20분 뒤 자동 종료됩니다. <br/>
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
                {isLoading && (
                  <div className='chat bot'>
                    <img className='bot-profile' src={require('../assets/chat-profile.png')} alt='bot-profile'/>
                    <div className='message'>
                      <div className='spinner'></div>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
          <div className='chat-input-wrapper'>
            <div className='chat-input-wrap-inner'>
              <input type='text' id='chat-input' placeholder='질문을 입력하세요.' disabled={isLoading} onKeyUp={(e) => {
                if (e.key === "Enter") {
                  document.getElementById("submit-btn").click();
                }
              }}/>
              <button id='submit-btn' disabled={isLoading} onClick={() => {
                let input = document.getElementById("chat-input");
                const content = input.value;
                if (content.length > 0) {
                  sendMessage(content);
                  input.value = "";
                }
              }}>
                <img src={require('../assets/arrow_circle_up.png')} alt='arrow_circle_up' />
              </button>
            </div>
          </div>
        </div>
        <div className='exit-btn-wrapper'>
          <button id='exit-btn' onClick={()=>navigate('/')}>
            상담 종료
          </button>
        </div>
      </div>
    </div>
  )
}

export default Consulting;