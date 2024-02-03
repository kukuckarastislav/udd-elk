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
          <div>
            <Button variant="outlined" size="small">
              <Link to={"/add/law"}>Add Law</Link>
            </Button>
          </div>
          <div>
            <Button variant="outlined" size="small">
              <Link to={"/add/contract"}>Add Contract</Link>
            </Button>
          </div>
          <div>
            <Button variant="outlined" size="small">
              <Link to={"/signup/gov"}>Signup Government</Link>
            </Button>  
          </div>
        </div>
      </div>
    </div>
  )
}
