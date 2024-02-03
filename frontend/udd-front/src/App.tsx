import { BrowserRouter, Route, Routes } from 'react-router-dom'
import style from './App.module.scss'
import Navbar from './components/Navbar'
import SearchPage from './pages/SearchPage'
import IndexLawPage from './pages/IndexLawPage'
import IndexContractPage from './pages/IndexContractPage'
import SignupGovPage from './pages/SignupGovPage'

function App() {
  
  return (
   <div className={style.AppCSS}>
      <BrowserRouter>
      <Navbar />
        <Routes>
          <Route id='1' path="/" element={<SearchPage />} />
          <Route id='2' path="/add/law" element={<IndexLawPage />} />
          <Route id='3' path="/add/contract" element={<IndexContractPage />} />
          <Route id='4' path="/signup/gov" element={<SignupGovPage />} />
        </Routes>
      </BrowserRouter>
   </div>
  )
}

export default App
