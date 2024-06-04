import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './NavigationMenu.css';
import { deleteCookie } from '../utils/cookieManage';

function NavigationMenu({ toggleDropdown }) {
  const navigate = useNavigate();
  const location = useLocation();
  const [activePage, setActivePage] = useState('');

  useEffect(() => {
    const path = location.pathname;
    const page = path.split("/").pop();
    setActivePage(page);
  }, [location]);

  const navItems = [
    { path: "", title: "나의 일기장" },
    { path: "analysis", title: "나의 감정분석" },
    { path: "login", title: "로그아웃" }
  ];

  const handleNavigation = (path) => {
    if (path.includes('login')) {
      deleteCookie('accessToken'); 
      deleteCookie('refreshToken'); 
    }
    navigate('/' + path);
    toggleDropdown();
  };

  return (
    <>
      {navItems.map((item, index) => (
        <li key={index} className={activePage === item.path ? 'active' : ''} onClick={() => handleNavigation(item.path)}>
            {item.title}
        </li>
      ))}
    </>
  );
}

export default NavigationMenu;
