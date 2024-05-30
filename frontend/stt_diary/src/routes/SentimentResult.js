import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getCookie } from '../utils/cookieManage';
import LoadingModal from '../components/LoadingModal';
import './SentimentResult.css';

function SentimentResult() {
  const { option, id } = useParams();
  const [isLoadingModalOpen, setIsLoadingModalOpen] = useState(false);  
  const [loadingMessage, setLoadingMessage] = useState('');
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
            return response.json();
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
        setResult(res.emotion);
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
    if (option === 'create') {
        setLoadingMessage('오늘 일기로 감정 분석 중입니다 ...');
    }
    else if (option === 'modify') {
        setLoadingMessage('수정된 일기로 감정 분석 중입니다 ...');
    }
    setIsLoadingModalOpen(true);
    requestResultAnalysis();
    setTimeout(() => {
          requestResult();
    }, 3000);
  }, [id]);

  return (
    <div className='sentiment-result-container'>
      {isLoadingModalOpen ? (
        <LoadingModal setIsLoadingModalOpen={setIsLoadingModalOpen} loadingMessage={loadingMessage} />
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
                    <button className='consulting-btn' onClick={()=>navigate(`/consulting/${id}`)}>상담하기</button>
                }
            </div>
        </div>
        )}
    </div>
  );
}
export default SentimentResult;