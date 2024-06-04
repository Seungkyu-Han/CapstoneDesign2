import './App.css';
import { useState, useEffect } from 'react';
import { Routes, Route, useNavigate, useLocation} from 'react-router-dom';
import DiaryList from './routes/DiaryList';
import SentimentReport from './routes/SentimentReport';
import NavigationMenu from './components/NavigationMenu';
import CreateDiary from './routes/CreateDiary';
import Consulting from './routes/Consulting';
import DetailDiary from './routes/DetailDiary';
import SentimentResult from './routes/SentimentResult';
import Login from './routes/Login';
import Redirect from './routes/Redirect';
import { getCookie, setCookie } from './utils/cookieManage'


function App() {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

  useEffect(() => {
    if (!getCookie("accessToken") && !getCookie("refreshToken")) {
      navigate('/login');
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
        })
        .catch(error => {
            alert("관리자에게 문의해주세요.");
        });
    }
    else {
      navigate('/');
    }
  },[]);

  if (location.pathname === '/login') {
    return <Login />;
  }

  return (
    <div className="App">
      <nav>
            <div className='mobileNavBar'>
              <img src={require('./assets/mobileLogo.png')} alt="" className='mobileLogo' />
              <button className="dropdown-btn" onClick={() => toggleDropdown()}>
                <img src={require('./assets/barIcon.png')} alt=""></img>
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
        <Route path='/consulting/:id' element={<Consulting/>} />
        <Route path='/detail-diary/:id' element={<DetailDiary/>}/>
        <Route path='/result/:option/:id' element={<SentimentResult />} />
        <Route path='/redirect' element={<Redirect/>}/>
      </Routes>    
    </div>
  );
}

export default App;
