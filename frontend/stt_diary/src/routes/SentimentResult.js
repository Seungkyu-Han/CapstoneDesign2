import React from 'react';
import './SentimentResult.css';

function SentimentResult() {
  const result = 1;
  if (result === 1) {
    return (
        <div className='sentiment-result-container'>
            <div className='positive-container'>
                <div className='result-title-container'>
                    <span className='title1'>감정 분석 결과는 </span>
                    <span className='title-positive'>긍정적</span>
                    <span className='title1'>이네요 </span>
                </div>
                <div className='result-content'>... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...</div>
            </div>
        </div>
    );
  } else if (result === 2) {
    return (
        <div className='sentiment-result-container'>
            <div className='negative-container'>
                <div className='result-title-container'>
                    <span className='title1'>감정 분석 결과는 </span>
                    <span className='title-negative'>부정적</span>
                    <span className='title1'>이네요 </span>
                </div>
                <div className='result-content-negative'>
                    <p>... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...감정분석 내용 ...... </p>
                    <button className='consulting-btn'>상담하기</button>
                </div>
            </div>
        </div>
    );
  } else {
    return (
        <div className='sentiment-result-container'>
            <div className='neutral-container'>
                <div className='result-title-container'>
                    <span className='title1'>감정 분석 결과는 </span>
                    <span className='title-neutral'>중립적</span>
                    <span className='title1'>이네요 </span>
                </div>
                <div className='result-content'>... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...... 감정분석 내용 ...</div>
            </div>
        </div>
    );
  }
}
export default SentimentResult;