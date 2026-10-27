export default function GameOver({ score, totalQuestions, onPlayAgain }) {
    const percentage = Math.round((score / totalQuestions) * 100);

    return (
        <>
            <h2>Game Over</h2>
            <p className="score-text">
                Your Score: <strong>{percentage}%</strong>
            </p>
            <button onClick={onPlayAgain} className="button">Play Again</button>
        </>
    );
}