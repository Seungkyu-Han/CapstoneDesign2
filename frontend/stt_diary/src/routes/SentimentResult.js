import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getCookie } from '../utils/cookieManage';
import LoadingModal from '../components/LoadingModal';
import './SentimentResult.css';

function SentimentResult() {
  const { id } = useParams(); 
  const [isLoadingModalOpen, setIsLoadingModalOpen] = useState(false);  
  const [result, setResult] = useState(null);
  const [resultAnalysis, setResultAnalysis] = useState(null);
  const navigate = useNavigate();

  const requestResult = (count = 0) => {
    setIsLoadingModalOpen(true);
    fetch(`${process.env.REACT_APP_API_URL}/api/feeling?diaryId=${id}`,
    {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getCookie('accessToken'),
        },
    })
    .then(response => {
        if (response.status === 200) {
            return response.text();
        } else {
            if(count < 14){
                setTimeout(() => requestResult(count + 1), 500);
            }
            else{
                setIsLoadingModalOpen(false);
                alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
                navigate('/');
            }
        }
    })
    .then((res) => {
        setResult(res);
    })
    .catch(error => {
        setIsLoadingModalOpen(false);
        alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
        navigate('/');
    });
  };

  const requestResultAnalysis = () => {
    fetch(`${process.env.REACT_APP_API_URL}/api/chatGPT/diary?diaryId=${id}`,
    {
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
        setResultAnalysis(res.content);
    })
    .catch(error => {
        alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
        navigate('/');
    });
  };

  useEffect(() => {
    if(resultAnalysis && result){
        setIsLoadingModalOpen(false);
    }
  }, [resultAnalysis, result]);

  useEffect(() => {
    setIsLoadingModalOpen(true);
    requestResultAnalysis();
    setTimeout(() => {
          requestResult();
    }, 3000);
  }, [id]);

  return (
    <div className='sentiment-result-container'>
      {isLoadingModalOpen ? (
        <LoadingModal setIsLoadingModalOpen={setIsLoadingModalOpen}/>
      ) : (
        <div className={`sentiment-result-inner ${result}`}>
            <h3 className='result-title'>
                감정 분석 결과는
                {result === 'positive' && <span className='title-positive'>긍정적</span>}
                {result === 'negative' && <span className='title-negative'>부정적</span>}
                {result === 'neutral' && <span className='title-neutral'>중립적</span>}
                이네요
            </h3>
            <div className='result-content'>
                <div className='result-content-inner'>
                    {resultAnalysis}
                </div>
                {result === 'negative' && 
                    <button className='consulting-btn'>상담하기</button>
                }
            </div>
        </div>
        )}
    </div>
  );
}
export default SentimentResult;