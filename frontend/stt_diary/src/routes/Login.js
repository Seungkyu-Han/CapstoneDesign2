import React from 'react';
import './Login.css';
import { useNavigate } from 'react-router-dom';
import { setCookie } from '../utils/cookieManage';

function Login() {
    const navigate = useNavigate();

    const login = () => {
        window.location.href = `https://kauth.kakao.com/oauth/authorize?&response_type=code&client_id=${process.env.REACT_APP_KAKAO_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_KAKAO_REDIRECT_URI}`;
        const loginCode = new URL(window.location).searchParams.get("code");
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
      };
    return (
        <div className='login-container'>
            <img className='img1' src={require('../assets/login1.png')} alt='' />
            <img className='img2' src={require('../assets/login2.png')} alt='' />
            <div className='login-btn' onClick={login}>
            <img src={require('../assets/kakaoBtn.png')} alt='' />
            </div>
        </div>
    );
}
export default Login;