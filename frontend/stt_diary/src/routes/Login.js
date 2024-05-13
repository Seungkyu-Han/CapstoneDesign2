import React from 'react';
import './Login.css';

function Login() {
    const login = () => {
        window.location.href = `https://kauth.kakao.com/oauth/authorize?&response_type=code&client_id=${process.env.REACT_APP_KAKAO_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_KAKAO_REDIRECT_URI}/redirect`;
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