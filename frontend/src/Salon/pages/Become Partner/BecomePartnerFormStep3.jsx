import React from "react";
import { TextField } from "@mui/material";
import Grid from "@mui/material/Grid";

const BecomePartnerFormStep2 = ({ formik }) => {
  return (
    <div>
      <Grid container spacing={3}>
        <Grid size={6}>
          <TextField
            fullWidth
            name="salonAddress.phoneNumber"
            label="Mobile"
            value={formik.values?.salonAddress.phoneNumber}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={
              formik.touched.phoneNumber && Boolean(formik.errors.phoneNumber)
            }
            helperText={formik.touched.phoneNumber && formik.errors.phoneNumber}
          />
        </Grid>
        <Grid size={6}>
          <TextField
            fullWidth
            name="salonAddress.pincode"
            label="Pincode"
            value={formik.values?.salonAddress.pincode}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={
              formik.touched.pickupAddress?.pincode &&
              Boolean(formik.errors.pickupAddress?.pincode)
            }
            helperText={
              formik.touched.pickupAddress?.pincode &&
              formik.errors.pickupAddress?.pincode
            }
          />
        </Grid>
        <Grid size={12}>
          <TextField
            fullWidth
            name="salonAddress.address"
            label="Address (House No, Building, Street)"
            value={formik.values?.salonAddress.address}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={
              formik.touched.pickupAddress?.address &&
              Boolean(formik.errors.pickupAddress?.address)
            }
            helperText={
              formik.touched.pickupAddress?.address &&
              formik.errors.pickupAddress?.address
            }
          />
        </Grid>

        <Grid size={12}>
          <TextField
            fullWidth
            name="salonAddress.city"
            label="City"
            value={formik.values?.salonAddress.city}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={
              formik.touched.pickupAddress?.city &&
              Boolean(formik.errors.pickupAddress?.city)
            }
            helperText={
              formik.touched.pickupAddress?.city &&
              formik.errors.pickupAddress?.city
            }
          />
        </Grid>
        <Grid size={12}>
          <TextField
            fullWidth
            name="salonAddress.email"
            label="Email"
            value={formik.values?.salonAddress.email}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={
              formik.touched.pickupAddress?.email &&
              Boolean(formik.errors.pickupAddress?.email)
            }
            helperText={
              formik.touched.pickupAddress?.email &&
              formik.errors.pickupAddress?.email
            }
          />
        </Grid>
      </Grid>
    </div>
  );
};

export default BecomePartnerFormStep2;
