import './App.css';
import { useState, useEffect } from 'react';
import { Routes, Route, useNavigate} from 'react-router-dom';
import DiaryList from './routes/DiaryList';
import SentimentReport from './routes/SentimentReport';
import NavigationMenu from './components/NavigationMenu';
import CreateDiary from './routes/CreateDiary';
import Consulting from './routes/Consulting';
import DetailDiary from './routes/DetailDiary';
import SentimentResult from './routes/SentimentResult';

const setCookie = (cookieName, cookieValue, expirationDays) =>{
  const date = new Date();
  date.setTime(date.getTime() + (expirationDays * 60 * 1000));
  const expires = "expires=" + date.toUTCString();
  const cookie = encodeURIComponent(cookieName) + "=" + encodeURIComponent(cookieValue) + ";" + expires + ";path=/";
  document.cookie = cookie;
}

export const getCookie = (name) => {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(name + '=')) {
            return cookie.substring(name.length + 1);
        }
    }
    return null;
}

function App() {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [loggedIn, setLoggedIn] = useState(false);
  const navigate = useNavigate();

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

  useEffect(() => {
    if (!getCookie("accessToken") && !getCookie("refreshToken")) {
      setLoggedIn(false);
    }
    else if (!getCookie("accessToken")) {
        fetch(`${process.env.REACT_APP_API_URL}/api/auth/login`, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getCookie("refreshToken")}`
          }
          })         
          .then(response => response.json())
          .then(res => {
            setCookie("accessToken", res.accessToken, 2 * 60);
            setCookie("refreshToken", res.refreshToken, 24 * 14 * 60);
            window.location.reload();
        })
        .catch(error => {
            alert("관리자에게 문의해주세요.");
        });
    }
    else {
      setLoggedIn(true);
    }
  },[loggedIn]);

  const login = () => {
    window.location.href = `https://kauth.kakao.com/oauth/authorize?&response_type=code`;
    const loginCode = new URL(window.location).searchParams.get("code");
    console.log(loginCode);
    if (loginCode) {
        fetch(`${process.env.REACT_APP_API_URL}/api/auth/login?code=${loginCode}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(response => response.json())
        .then(res => {
            setCookie('accessToken', res.accessToken, 2 * 60);
            setCookie('refreshToken', res.refreshToken, 24 * 60 * 7);
            navigate('/');
        })
        .catch(error => {
          alert("관리자에게 문의해주세요.");
        });
    } else {
        alert('잘못된 접근입니다.');
    }
  }

  return (
    loggedIn ? (
        <div className="App">
            <nav>
                <div className='mobileNavBar'>
                    <img src={require('./assets/mobileLogo.png')} alt="" className='mobileLogo' />
                    <button className="dropdown-btn" onClick={() => toggleDropdown()}>
                        <img src={require('./assets/barIcon.png')} alt="" />
                    </button>
                    <ul className={`dropdown-content ${isDropdownOpen ? 'show' : ''}`}>
                        <NavigationMenu toggleDropdown={toggleDropdown} />
                    </ul>
                </div>
                <ul className="navbar">
                    <NavigationMenu toggleDropdown={toggleDropdown} />
                </ul>
            </nav>
            <Routes>
        <Route path='/' element={<DiaryList />}/>
        <Route path='/analysis' element={<SentimentReport/>}/>
        <Route path='/create-diary' element={<CreateDiary/>}/>
        <Route path='/consulting' element={<Consulting/>} />
        <Route path='/detail-diary/:id' element={<DetailDiary/>}/>
        <Route path='/result' element={<SentimentResult/>}/>
            </Routes>
        </div>
    ) : (
        <div className='login-container'>
            <img className='img1' src={require('./assets/login1.png')} alt='' />
            <img className='img2' src={require('./assets/login2.png')} alt='' />
            <div className='login-btn' onClick={login}>
              <img src={require('./assets/kakaoBtn.png')} alt='' />
            </div>
        </div>
    )
);
}

export default App;
