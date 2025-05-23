import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import CustomerOrder from "./pages/CustomerOrder";
import KitchenPage from "./pages/KitchenPage";
import PaymentPage from "./pages/PaymentPage";
import ManagerPage from "./pages/ManagerPage";
import AssignCustomer from "./pages/EmployeeAssignTable";
import OrderStatusByTable from "./pages/OrderStatusByTable";

import RegisterPage from "./pages/RegisterPage ";


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/customer" element={<AssignCustomer />} />
        <Route path="/order" element={<CustomerOrder />} />
        <Route path="/kitchen" element={<KitchenPage />} />
        <Route path="/payment" element={<PaymentPage />} />
        <Route path="/manager" element={<ManagerPage />} />
        <Route path="/serve" element={<OrderStatusByTable />} />
      </Routes>
    </Router>
  );
}

export default App;
