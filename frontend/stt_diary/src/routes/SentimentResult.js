import React from 'react';
import './SentimentResult.css';

function SentimentResult() {
  const result = 3;
  let resultClassName = '';
  if (result === 1) {
    resultClassName = 'positive';
  }else if (result === 2) {
    resultClassName = 'negative';
  }else if (result === 3) {
    resultClassName = 'neutral';}
  return (
    <div className='sentiment-result-container'>
        <div className={`sentiment-result-inner ${resultClassName}`}>
            <h3 className='result-title-container'>
                <span className='title1'>감정 분석 결과는 </span>
                {result === 1 && <span className='title-positive'>긍정적</span>}
                {result === 2 && <span className='title-negative'>부정적</span>}
                {result === 3 && <span className='title-neutral'>중립적</span>}
                <span className='title1'>이네요 </span>
            </h3>
            <div className='result-content'>
                <p>... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...감정분석 내용 ...... </p>
                {result === 2 && 
                    <button className='consulting-btn'>상담하기</button>
                }
            </div>
        </div>
    </div>
  );
}
export default SentimentResult;