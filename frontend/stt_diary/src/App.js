import './App.css';
import { useState } from 'react';
import { Routes, Route} from 'react-router-dom';
import DiaryList from './routes/DiaryList';
import SentimentReport from './routes/SentimentReport';
import NavigationMenu from './components/NavigationMenu';
import CreateDiary from './routes/CreateDiary';
import Consulting from './routes/Consulting';
import DetailDiary from './routes/DetailDiary';

function App() {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

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
        <Route path='/consulting' element={<Consulting/>} />
        <Route path='/detail-diary/:id' element={<DetailDiary/>}/>
      </Routes>    
    </div>
  );
}

export default App;
