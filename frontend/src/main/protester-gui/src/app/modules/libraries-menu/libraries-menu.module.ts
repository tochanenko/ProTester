import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LibrariesMenuRoutingModule } from './libraries-menu-routing.module';
import { ViewComponent } from './view/view.component';
import {LibrariesModule} from "./libraries/libraries.module";
import { CompoundsModule } from "./compounds/compounds.module";


@NgModule({
  declarations: [ViewComponent],
  imports: [
    CommonModule,
    LibrariesMenuRoutingModule,
    LibrariesModule,
    CompoundsModule
  ]
})
export class LibrariesMenuModule { }
