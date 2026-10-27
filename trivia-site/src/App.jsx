import { useState } from 'react';
import './App.css';
import { useTrivia } from './hooks/useTrivia';
import SettingsModal from './components/SettingsModal';
import QuestionCard from './components/QuestionCard';
import GameOver from './components/GameOver';

const INITIAL_SETTINGS = { amount: 10, category: 0, difficulty: 'any' };

export default function App() {
  const [showSettings, setShowSettings] = useState(false);

  const {
    questions, currentIndex, score, status, errorMessage,
    settings, answerQuestion, restartGame,
  } = useTrivia(INITIAL_SETTINGS);

  const handleSaveSettings = (newSettings) => {
    setShowSettings(false);
    restartGame(newSettings);
  };

  return (
      <>
        <button onClick={() => setShowSettings(true)} className="settings-button">
          Settings
        </button>

        {showSettings && (
            <SettingsModal
                currentSettings={settings}
                onSave={handleSaveSettings}
                onClose={() => setShowSettings(false)}
            />
        )}

        <div className="container">
          {status === 'loading' && <h2>Loading...</h2>}

          {status === 'submitting' && <h2>Calculating your score...</h2>}

          {status === 'error' && (
              <>
                <p>{errorMessage}</p>
                <button onClick={() => restartGame(settings)} className="button">
                  Try Again
                </button>
              </>
          )}

          {status === 'finished' && (
              <GameOver
                  score={score}
                  totalQuestions={questions.length}
                  onPlayAgain={() => restartGame(settings)}
              />
          )}

          {status === 'playing' && (
              <QuestionCard
                  questionData={questions[currentIndex]}
                  currentIndex={currentIndex}
                  totalQuestions={questions.length}
                  onAnswer={answerQuestion}
              />
          )}
        </div>
      </>
  );
}