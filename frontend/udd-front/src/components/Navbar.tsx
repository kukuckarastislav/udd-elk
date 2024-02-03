import { Button } from '@mui/material'
import style from './Navbar.module.scss'
import { Link } from 'react-router-dom'

export default function Navbar() {
  return (
    <div className={style.NavbarCss}>
      <div className="mx-40 flex gap-40 items-center w-full">
        <div className="font-bold text-2xl cursor-pointer">
          <Link to={"/"}>ZAKONKO</Link>
        </div>
        <div className='flex gap-4'>
          <div className='cool-link'>
            <Link to={"/add/law"}>Add Law</Link>
          </div>
          <div className='cool-link'>
            <Link to={"/add/contract"}>Add Contract</Link>
          </div>
          <div className='cool-link'>
            <Link to={"/signup/gov"}>Signup Government</Link>
          </div>
        </div>
      </div>
    </div>
  )
}
