import React, {useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import toolService from "../services/tool.service";
import '../App.css';

const Start = () => {

    return (
        <div className="d-grid gap-2 col-6 mx-auto">
            <h2 className="text-center my-4">Bienvenido, por favor indica tu rol</h2>
        <button className="btn bg-custom-yellow" type="button">Administrador</button>
        <button className="btn btn-primary" type="button">Atención al cliente</button>
        </div>
    );
}

export default Start;